-- Insert Activities
INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES
('ImpulsoTIC Week', 2024, 'closed', '2024-02-15', '2024-02-15', 'Central Park'),
('Informatics Olimpics', 2024, 'planned', '2024-03-10', '2024-03-12', 'Convention Center');

-- Insert SponsorOrganizations
INSERT INTO SponsorOrganizations (name, address, nif, vat) VALUES
('Tech Corp', '123 Tech Street', 'A12345678', 'VAT123'),
('City Council', '456 Main Square', 'B87654321', 'VAT456');

-- Insert SponsorContacts
INSERT INTO SponsorContacts (idSponsorOrganization, name, email, phone) VALUES
(1, 'John Doe', 'johndoe@example.local', '+34911234567'),
(2, 'Mary White', 'marywhite@example.local', '+34912345678');

-- Insert GBMembers
INSERT INTO GBMembers (name, role) VALUES
('Rosa', 'Secretary');;

-- Insert Levels
INSERT INTO Levels (idActivity, name, fee) VALUES
(2, 'Silver', 3000.00),
(1, 'Basic', 1000.00);

-- Insert SponsorshipAgreements
INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES
(2, 1, 2, 3000.00, '2024-01-15', 'signed');