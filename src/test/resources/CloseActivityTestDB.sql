-- Insert SponsorOrganizations
INSERT INTO SponsorOrganizations (name, address, nif, vat) VALUES
('Tech Corp', '123 Tech Street', 'A12345678', 'VAT123');

-- Insert SponsorContacts
INSERT INTO SponsorContacts (idSponsorOrganization, name, email, phone) VALUES
(1, 'John Doe', 'johndoe@example.local', '+34911234567');

-- Insert GBMembers
INSERT INTO GBMembers (name, role) VALUES
('Rosa', 'Secretary');

-- Insert Activities
INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES
('Informatics Olympics', 2023, 'closed' , '2023-01-01', '2023-01-10', 'Convention Center'),
('Informatics Olympics', 2024, 'planned', '2024-01-01', '2024-01-10', 'Convention Center'),
('ImpulsoTIC Week'     , 2023, 'planned', '2023-01-01', '2023-01-10', 'Central Park'),
('ImpulsoTIC Week'     , 2024, 'planned', '2024-01-01', '2024-01-10', 'Central Park'),
('ImpulsoTIC Week'     , 2025, 'planned', '2025-01-01', '2025-01-10', 'Central Park');

-- Insert Levels
INSERT INTO Levels (idActivity, name, fee) VALUES
(1, 'Basic', 1000.00),
(2, 'Basic', 1000.00),
(3, 'Basic', 1000.00),
(4, 'Basic', 1000.00),
(5, 'Basic', 1000.00);

-- Insert SponsorshipAgreements
INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES
(1, 1, 1, 1000.00, '2023-01-01', 'closed'),
(1, 1, 2, 1000.00, '2024-01-01', 'closed'),
(1, 1, 3, 1000.00, '2023-01-01', 'signed'),
(1, 1, 4, 1000.00, '2024-01-01', 'closed'),
(1, 1, 5, 1000.00, '2025-01-01', 'closed');

-- Insert Invoices
INSERT INTO Invoices (id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate, status) VALUES
('INV-001', 1, '2023-01-01', 1000.00, 21, 'paid'  ),
('INV-002', 2, '2024-01-01', 1000.00, 21, 'paid'  ),
('INV-004', 4, '2024-01-01', 1000.00, 21, 'paid'  ),
('INV-005', 5, '2025-01-01', 1000.00, 21, 'paid'  );

-- Insert SponsorshipPayments
INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES
('INV-001', '2023-01-01', 1000.00 * 1.21),
('INV-002', '2024-01-01', 1000.00 * 1.21),
('INV-004', '2024-01-01', 1000.00 * 1.21),
('INV-005', '2025-01-01', 1000.00 * 1.21); 

-- Insert sample data into IncomesExpenses (ensuring all have at least one movement)
INSERT INTO IncomesExpenses (idActivity, type, status, amountEstimated, dateEstimated, concept) VALUES 
(1, 'income' , 'paid'     ,  100.00, '2023-01-01', 'Other income for Informatics Olympics 2023'),
(1, 'expense', 'paid'     , -100.00, '2023-01-01',      'Expense for Informatics Olympics 2023'),
(2, 'income' , 'paid'     ,  100.00, '2024-01-01', 'Other income for Informatics Olympics 2024'),
(2, 'expense', 'paid'     , -100.00, '2024-01-01',      'Expense for Informatics Olympics 2024'),
(3, 'income' , 'paid'     ,  100.00, '2023-01-01', 'Other income for ImpulsoTIC Week      2023'),
(3, 'expense', 'paid'     , -100.00, '2023-01-01',      'Expense for ImpulsoTIC Week      2023'),
(4, 'income' , 'estimated',  100.00, '2024-01-01', 'Other income for ImpulsoTIC Week      2024'),
(4, 'expense', 'paid'     , -100.00, '2024-01-01',      'Expense for ImpulsoTIC Week      2024'),
(5, 'income' , 'paid'     ,  100.00, '2025-01-01', 'Other income for ImpulsoTIC Week      2025'),
(5, 'expense', 'estimated', -100.00, '2025-01-01',      'Expense for ImpulsoTIC Week      2025');

-- Insert sample data into Movements (ensuring all incomes/expenses have at least one movement)
INSERT INTO Movements (idType, concept, amount, date) VALUES 
(1, 'Payment for other income for Informatics Olympics 2023',  100.00, '2023-01-01'),
(2, 'Payment for      expense for Informatics Olympics 2023', -100.00, '2023-01-01'),
(3, 'Payment for other income for Informatics Olympics 2024',  100.00, '2024-01-01'),
(4, 'Payment for      expense for Informatics Olympics 2024', -100.00, '2024-01-01'),
(5, 'Payment for other income for ImpulsoTIC Week      2023',  100.00, '2023-01-01'),
(6, 'Payment for      expense for ImpulsoTIC Week      2023', -100.00, '2023-01-01'),
(8, 'Payment for      expense for ImpulsoTIC Week      2024', -100.00, '2024-01-01'),
(9, 'Payment for other income for ImpulsoTIC Week      2025',  100.00, '2025-01-01');
