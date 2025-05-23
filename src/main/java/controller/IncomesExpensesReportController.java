package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import DTOs.ActivitiesDTO;
import model.ActivitiesModel;
import model.MovementsModel;
import model.SponsorshipAgreementsModel;
import util.ModelManager;
import util.SwingMain;
import util.SwingUtil;
import util.SyntacticValidations;
import view.IncomesExpensesReportView;

public class IncomesExpensesReportController {
    
    protected MovementsModel movementsModel;
    protected ActivitiesModel activitiesModel;
    protected SponsorshipAgreementsModel saModel;
    
    protected IncomesExpensesReportView view; 
        
    public IncomesExpensesReportController(IncomesExpensesReportView v) { 
        this.movementsModel = ModelManager.getInstance().getMovementsModel();
        this.activitiesModel = ModelManager.getInstance().getActivitiesModel();
        this.saModel = ModelManager.getInstance().getSponsorshipAgreementsModel();
        
        this.view = v;
        this.initView();
        this.initController();        
    }

    public void initController() {
    	view.getFilterButton().addActionListener(e -> applyFilters());
        
        this.view.getButtonLowLeft().addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		SwingUtil.exceptionWrapper(() -> { view.disposeView(); });
        	}
        });
        
        this.view.getStartDateField().getDocument().addDocumentListener(new DocumentListener() {
        	@Override
        	public void insertUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> checkTextFields());
        	}
        	
        	@Override
        	public void removeUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> checkTextFields());
        	}
        	
        	@Override
        	public void changedUpdate(DocumentEvent e) {}
        });
        
        this.view.getEndDateField().getDocument().addDocumentListener(new DocumentListener() {
        	@Override
        	public void insertUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> checkTextFields());
        	}
        	
        	@Override
        	public void removeUpdate(DocumentEvent e) {
        		SwingUtil.exceptionWrapper(() -> checkTextFields());
        	}
        	
        	@Override
        	public void changedUpdate(DocumentEvent e) {}
        });
    }
    
    public void initView() {
    	view.getFilterButton().setEnabled(true);
    	showCurrentData();
        view.setVisible();
    }
    
    public void showCurrentData() {
        // Table    
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(SwingMain.getTodayDate());
        String startDate = calendario.get(Calendar.YEAR) + "-01-01";
        String endDate = calendario.get(Calendar.YEAR) + "-12-31";
        
        view.getStartDateField().setText(startDate);
        view.getEndDateField().setText(endDate);
        
        List<ActivitiesDTO> currentYearActivities = activitiesModel.getActivitiesFromCurrentYear(startDate, endDate);
        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Name", "Status", "Income Estimated", "Income Paid", "Expense Estimated", "Expense Paid", "Balance Estimated", "Balance Paid"}, 0);
        
        double totalEstimatedIncomes = 0;
        double totalEstimatedExpenses = 0;
        double totalPaidIncomes = 0;
        double totalPaidExpenses = 0;
        
        for (ActivitiesDTO activity : currentYearActivities) {
            double ei = movementsModel.getEstimatedIncome(activity.getId()) + saModel.getEstimatedSponshorships(activity.getId());
            double pi = movementsModel.getActualIncome(activity.getId()) + saModel.getActualSponshorships(activity.getId());
            double ee = movementsModel.getEstimatedExpenses(activity.getId());
            double pe = movementsModel.getActualExpenses(activity.getId());
            
            tableModel.addRow(new Object[]{
                activity.getDateStart() + "-" + activity.getDateEnd(),
                activity.getName(),
                activity.getStatus(),
                String.format("%.2f", ei),
                String.format("%.2f", pi),
                String.format("%.2f", ee),
                String.format("%.2f", pe),
                String.format("%.2f", ei + ee),
                String.format("%.2f", pi + pe)
            });
            
            totalEstimatedIncomes += ei;
            totalEstimatedExpenses += ee;
            totalPaidIncomes += pi;
            totalPaidExpenses += pe;
        }
        
        view.getReportTable().setModel(tableModel);
        
        view.getReportTable().getColumnModel().getColumn(1).setPreferredWidth(200);
        
        // Totals
        view.getTotalEstimatedIncomeLabel().setText("Estimated Income: " + totalEstimatedIncomes);
        view.getTotalPaidIncomeLabel().setText("Paid Income: " + totalPaidIncomes);
        view.getTotalEstimatedExpensesLabel().setText("Estimated Expenses: " + totalEstimatedExpenses);
        view.getTotalPaidExpensesLabel().setText("Paid Expenses: " + totalPaidExpenses);
    }

    
    public void checkTextFields() {
    	boolean valid = true;
		if (!SyntacticValidations.isDate(view.getStartDateField().getText())) {
			view.getStartDateField().setForeground(Color.RED);
			valid = false;
		} 
		else {
			view.getStartDateField().setForeground(Color.BLACK);
		}
		
		if (!SyntacticValidations.isDate(view.getEndDateField().getText())) {
    		view.getEndDateField().setForeground(Color.RED);
			valid = false;
		}
		else {
    		view.getEndDateField().setForeground(Color.BLACK);
		}
    	
		view.getFilterButton().setEnabled(valid);
    }
    
    private void applyFilters() {
    	String startDate = view.getStartDateField().getText();
        String endDate = view.getEndDateField().getText();
        String status = (String) view.getStatusComboBox().getSelectedItem();

        List<ActivitiesDTO> filteredActivities = activitiesModel.getFilteredActivities(startDate, endDate, status);
        
        update(filteredActivities);
    }
    
    private void update(List<ActivitiesDTO> filteredActivities) {
    	// Table
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Name", "Status", "Income Estimated", "Income Paid", "Expense Estimated", "Expense Paid", "Balance Estimated", "Balance Paid"}, 0);
        
        double totalEstimatedIncomes = 0;
        double totalEstimatedExpenses = 0;
        double totalPaidIncomes = 0;
        double totalPaidExpenses = 0;
        
        for (ActivitiesDTO activity : filteredActivities) {
        	double ei = movementsModel.getEstimatedIncome(activity.getId()) + saModel.getEstimatedSponshorships(activity.getId());
        	double pi = movementsModel.getActualIncome(activity.getId()) + saModel.getActualSponshorships(activity.getId());
        	double ee = movementsModel.getEstimatedExpenses(activity.getId());
        	double pe = movementsModel.getActualExpenses(activity.getId());
        	
        	tableModel.addRow(new Object[]{
                activity.getDateStart() + "-" + activity.getDateEnd(),
                activity.getName(),
                activity.getStatus(),
                String.format("%.2f", ei),
                String.format("%.2f", pi),
                String.format("%.2f", ee),
                String.format("%.2f", pe),
                String.format("%.2f", ei + ee),
                String.format("%.2f", pi + pe)
            });
            
        	totalEstimatedIncomes += ei;
            totalEstimatedExpenses += ee;
            totalPaidIncomes += pi;
            totalPaidExpenses += pe;
        }
        
        view.getReportTable().setModel(tableModel);
        
        view.getReportTable().getColumnModel().getColumn(1).setPreferredWidth(200);

        // Totals
        view.getTotalEstimatedIncomeLabel().setText(String.format("Estimated Income: %.2f  ", totalEstimatedIncomes));
        view.getTotalPaidIncomeLabel().setText(String.format("Paid Income: %.2f  ", totalPaidIncomes));
        view.getTotalEstimatedExpensesLabel().setText(String.format("Estimated Expenses: %.2f  ", totalEstimatedExpenses));
        view.getTotalPaidExpensesLabel().setText(String.format("Paid Expenses: %.2f  ", totalPaidExpenses));
    }
}
