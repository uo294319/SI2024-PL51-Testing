package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import controller.*;
import view.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

/**
 * Entry point for the app that includes the buttons to the execution of views
 * and DB inicialization.
 */
public class SwingMain {
	
	private static final boolean ACTIVE_DEBUG = true;
	
	private static Date today;

	private JFrame frame;
	
	private static final String APP_NAME = "Sponsor Management";
	private static final String DISCLAIMER = "<html>"
			+ "<p style='text-align:right;'>"
            + "This application was developed as part of a university project.<br>"
            + "Created by Team SI2024-PL51.<br>"
            + "Information Systems - University of Oviedo (2024-2025)."
            + "</p></html>";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //NOSONAR codigo autogenerado
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); //NOSONAR codigo autogenerado
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingMain() {
		SwingMain.today = new Date();
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Database db=new Database();
		
	    frame = new JFrame();
	    frame.setTitle(APP_NAME);
	    frame.setBounds(0, 0, 300, 300);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window

	    // Main panel with BorderLayout
	    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add margins
	    frame.getContentPane().add(mainPanel);

	    // Pannel at the top with title and change date field
	    JPanel topPanel = new JPanel(new BorderLayout());
	    
	    // Creates the title
        JLabel titleLabel = new JLabel(APP_NAME, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Creates the field for changing the date
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        JTextField textField = new JTextField(Util.dateToIsoString(SwingMain.today), 15);
        JButton button = new JButton("Change date of today");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(SyntacticValidations.isDate(textField.getText())){
            		Date newDate = Util.isoStringToDate(textField.getText());
                    SwingMain.today = newDate;
                    JOptionPane.showMessageDialog(null, "Date updated to: " + Util.dateToIsoString(newDate));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(textField);
        inputPanel.add(button);
        
        if(ACTIVE_DEBUG) topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

	    // Button panel with vertical BoxLayout
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    
	    /* --------------------------------------------------------------------------------
	     * 
	     *     START ADD BUTTONS
	     */	   
	    
	    // Sponsorship Management Subsystem
	    addButtonToMain(buttonPanel, "Sponsorship Management", () -> {
	    	SubmenuView menu = new SubmenuView("Sponsorship Management");
	    	
	    	// US 29124
	    	menu.addButton("Register Sponshorship", () -> {
		    	new RegisterSponsorshipController(new RegisterSponsorshipView());
		    });
	    	
	    	// US 29254
	    	menu.addButton("Register Long-Term Sponshorship", () -> {
		    	new RegisterLongTermAgreementController(new RegisterLongTermAgreementView());
		    }, false);
	    	
	    	// US 29125
	    	menu.addButton("Generate Invoices", () -> {
	    		new GenerateInvoicesController(new GenerateInvoicesView());
		    });
	    	
	    	// US 29126
	    	menu.addButton("Register Payment", () -> {
	    		new RegisterPaymentController(new RegisterPaymentView());
	    	});
	    	
	    	// US 29329
	    	menu.addButton("Register Sponsor", () -> {
	    		new RegisterSponsorController(new RegisterSponsorView());
		    });
	    	
	    	// US 29325
	    	menu.addButton("Modify Sponsor Contact", () -> {
	    		new ModifySponsorContactController(new ModifySponsorContactView());
		    });
	    	
	    	menu.setVisible();
	    });
	    
	    
	    // Sponsorship Management Subsystem
	    addButtonToMain(buttonPanel, "Activity Management", () -> {
	    	SubmenuView menu = new SubmenuView("Activity Management");
	    	
	        // US 29127
	    	menu.addButton("Register Income/Expense", () -> {
	    		new RegisterMovementsController(new RegisterMovementsView());
	    	});
	    	
	    	// US 29128
	    	menu.addButton("Consult Status Activity", () -> {
	    		new ConsultStatusActivityController(new ConsultStatusActivityView());
	    	});
	    	
		    // US 29129
	    	menu.addButton("Incomes/Expenses Report", () -> {
		    	new IncomesExpensesReportController(new IncomesExpensesReportView());
	    	});
	    	
	    	// US 29332
	    	menu.addButton("Register Activity", () -> {
	    		new RegisterActivityController(new RegisterActivityView());
	    	});
	    	

	    	// US 29330
	    	menu.addButton("Modify Activity", () -> {
	    		new ModifyActivityController(new ModifyActivityView());
	    	});
	    	
	    	// US 29331
	    	menu.addButton("Close Activity", () -> {
	    		new CloseActivityController(new CloseActivityView());
		    });
	    	
	    	menu.setVisible();
	    });
	    
	    
	    if(ACTIVE_DEBUG)
	    {
	    	buttonPanel.add(Box.createVerticalStrut(10));
		    buttonPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		    buttonPanel.add(Box.createVerticalStrut(20));
		    
	    	addButtonToMain(buttonPanel, "Initialize Empty Database", () -> {
		        db.createDatabase(false);
		    });
		    
		    addButtonToMain(buttonPanel, "Load data for testing", () -> {
				db.createDatabase(false);
				db.loadDatabase();
		    });
		    
		    addButtonToMain(buttonPanel, "Debugger", () -> {
				new Debugger();
		    });
	    }

	    
	    /* 
	     *     END ADD BUTTONS
	     * 
	     * -------------------------------------------------------------------------------- */

	    mainPanel.add(buttonPanel, BorderLayout.CENTER);

	    // Disclaimer at the bottom
	    JLabel disclaimerLabel = new JLabel(DISCLAIMER);
	    disclaimerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
	    disclaimerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

	    mainPanel.add(disclaimerLabel, BorderLayout.SOUTH);
	    
	    // Adjust frame size to fit content
	    frame.pack();
	    
	    // Set minimum size to prevent shrinking below initial dimensions
	    frame.setMinimumSize(frame.getSize());
	}

	/**
	 * Creates a button in the main frame
	 * @param text to be displied inside the button
	 * @param action to be performed by the button on-click
	 */
	private void addButtonToMain(JPanel panel, String text, Runnable action, boolean enabled) {
	    JButton button = new JButton(text);
	    button.addActionListener(e -> action.run());
	    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.setEnabled(enabled);
	    panel.add(button);
	    panel.add(Box.createVerticalStrut(10)); // Add spacing between buttons
	}
	
	/**
	 * Creates a button in the main frame
	 * @param text to be displied inside the button
	 * @param action to be performed by the button on-click
	 */
	private void addButtonToMain(JPanel panel, String text, Runnable action) {
		addButtonToMain(panel, text, action, true);
	}


	public JFrame getFrame() { return this.frame; }
	
	public static Date getTodayDate() {  return SwingMain.today; }
	public static void setTodayDate(Date today)
	{
		SwingMain.today = today;
	}
	
}