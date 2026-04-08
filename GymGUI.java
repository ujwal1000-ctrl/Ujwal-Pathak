package Coursework.GymGUICoursework;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * GymGUI is the main graphical user interface for the Gym Management System.
 * <p>
 * This class allows users to manage gym members, including both Regular and Premium members.
 * It provides features for adding, activating, deactivating, reverting, and displaying members,
 * as well as handling plan upgrades, attendance, payment, and discount calculations.
 * The interface is built using Java Swing components and supports data validation and user feedback.
 * </p>
 * <p>
 * Main Features:
 * <ul>
 *   <li>Add, activate, deactivate, and revert Regular and Premium members</li>
 *   <li>Mark attendance and manage loyalty points</li>
 *   <li>Upgrade plans for regular members</li>
 *   <li>Process payments and calculate discounts for premium members</li>
 *   <li>Display all active members with detailed information</li>
 *   <li>Save member details to a file</li>
 * </ul>
 * </p>
 * <p>
 * Dependencies: javax.swing, java.io, java.util, and supporting member classes (RegularMember, PremiumMember, GymMember)
 * </p>
 */
public class GymGUI extends JFrame {
    private ArrayList<GymMember> membersList = new ArrayList<>();
    
    // GUI Components
    private JTextField idField, nameField, locationField, phoneField, emailField;
    private JTextField referralSourceField, paidAmountField, trainerNameField;
    private JTextField regularPlanPriceField, premiumPlanChargeField, discountAmountField;
    private JTextArea removalReasonField;  // Change from JTextField to JTextArea
    private JScrollPane removalReasonScrollPane;
    private JComboBox<String> dobYearComboBox, dobMonthComboBox, dobDayComboBox;
    private JComboBox<String> msYearComboBox, msMonthComboBox, msDayComboBox;
    private JComboBox<String> planComboBox;
    private JRadioButton maleRadio, femaleRadio, otherRadio;
    private ButtonGroup genderGroup;
    private JButton addRegularBtn, addPremiumBtn, activateBtn, deactivateBtn;
    private JButton markAttendanceBtn, upgradePlanBtn, calculateDiscountBtn, payDueBtn;
    private JButton revertRegularBtn, revertPremiumBtn, displayBtn, clearBtn, saveBtn;
    private JButton readFileBtn; // Button to read from file

    public GymGUI() {
        super("Gym Management System");
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        readFromFile(); // Load members from file on startup
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        // Text Fields
        idField = new JTextField();
        nameField = new JTextField();
        locationField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        referralSourceField = new JTextField();
        paidAmountField = new JTextField();
        trainerNameField = new JTextField();
        
        // Non-editable fields
        regularPlanPriceField = new JTextField("0.00");
        regularPlanPriceField.setEditable(false);
        premiumPlanChargeField = new JTextField("50000.00");
        premiumPlanChargeField.setEditable(false);
        discountAmountField = new JTextField("0.00");
        discountAmountField.setEditable(false);

        // Text Area for Removal Reason
        removalReasonField = new JTextArea();
        removalReasonField.setLineWrap(true);
        removalReasonField.setWrapStyleWord(true);
        removalReasonScrollPane = new JScrollPane(removalReasonField);
        removalReasonScrollPane.setBounds(130, 240, 300, 60);

        // Date Combo Boxes
        initializeDateComboBoxes();

        // Plan Combo Box
        planComboBox = new JComboBox<>(new String[]{"Select Plan", "Basic", "Standard", "Deluxe"});
        planComboBox.setSelectedIndex(0);
        planComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPlan = (String) planComboBox.getSelectedItem();
                if (selectedPlan != null) {
                    switch(selectedPlan) {
                        default:
                            regularPlanPriceField.setText("0.00");
                            break;  
                        case "Basic":
                            regularPlanPriceField.setText("6500.00");
                            break;
                        case "Standard":
                            regularPlanPriceField.setText("12000.00");
                            break;
                        case "Deluxe":
                            regularPlanPriceField.setText("15000.00");
                            break;

                    }
                }
            }
        });

        // Gender Radio Buttons
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        otherRadio = new JRadioButton("Other");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);

        // Buttons
        addRegularBtn = new JButton("Add Regular Member");
        addPremiumBtn = new JButton("Add Premium Member");
        activateBtn = new JButton("Activate Membership");
        deactivateBtn = new JButton("Deactivate Membership");
        markAttendanceBtn = new JButton("Mark Attendance");
        upgradePlanBtn = new JButton("Upgrade Plan");
        calculateDiscountBtn = new JButton("Calculate Discount");
        payDueBtn = new JButton("Pay Due Amount");
        revertRegularBtn = new JButton("Revert Regular Member");
        revertPremiumBtn = new JButton("Revert Premium Member");
        displayBtn = new JButton("Display Members");
        clearBtn = new JButton("Clear");
        saveBtn = new JButton("Save to File");
        readFileBtn = new JButton("Read From File"); // Initialize button
    }

    private void initializeDateComboBoxes() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[101];
        for (int i = 0; i <= 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }
        
        dobYearComboBox = new JComboBox<>(years);
        dobMonthComboBox = new JComboBox<>(months);
        dobDayComboBox = new JComboBox<>(days);
        
        msYearComboBox = new JComboBox<>(years);
        msMonthComboBox = new JComboBox<>(months);
        msDayComboBox = new JComboBox<>(days);
    }

    private void setupLayout() {
        setLayout(null);
        setSize(1000, 800);
    
        // Main Panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBounds(0, 0, 1000, 800);
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createTitledBorder("Gym Management System"));
        add(mainPanel);
    
        // Left Panel (Member Information)
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(20, 40, 460, 400);
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Member Information"));
        mainPanel.add(leftPanel);

        // Add components to left panel with labels
        JLabel idLabel = new JLabel("Member ID:");
        idLabel.setBounds(20, 30, 100, 25);
        idField.setBounds(130, 30, 150, 25);
        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 65, 100, 25);
        nameField.setBounds(130, 65, 150, 25);
        
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(20, 100, 100, 25);
        locationField.setBounds(130, 100, 150, 25);
        
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(20, 135, 100, 25);
        phoneField.setBounds(130, 135, 150, 25);
        
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 170, 100, 25);
        emailField.setBounds(130, 170, 150, 25);
        
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(20, 205, 100, 25);
        JPanel genderPanel = new JPanel(null);
        genderPanel.setBounds(130, 205, 300, 25);
        genderPanel.setBackground(Color.LIGHT_GRAY);
        maleRadio.setBounds(0, 0, 80, 25);
        femaleRadio.setBounds(90, 0, 80, 25);
        otherRadio.setBounds(180, 0, 80, 25);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        genderPanel.add(otherRadio);

        JLabel removalReasonLabel = new JLabel("Removal Reason:");
        removalReasonLabel.setBounds(20, 240, 100, 25);

        // Add all components to left panel
        leftPanel.add(idLabel);
        leftPanel.add(idField);
        leftPanel.add(nameLabel);
        leftPanel.add(nameField);
        leftPanel.add(locationLabel);
        leftPanel.add(locationField);
        leftPanel.add(phoneLabel);
        leftPanel.add(phoneField);
        leftPanel.add(emailLabel);
        leftPanel.add(emailField);
        leftPanel.add(genderLabel);
        leftPanel.add(genderPanel);
        leftPanel.add(removalReasonLabel);
        leftPanel.add(removalReasonScrollPane);

        // Right Panel (Member Details)
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBounds(500, 40, 460, 400);
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Member Details"));
        mainPanel.add(rightPanel);

        // Add components to right panel
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(20, 30, 100, 25);
        JPanel dobPanel = new JPanel(null);
        dobPanel.setBounds(130, 30, 300, 25);
        dobPanel.setBackground(Color.LIGHT_GRAY);
        dobYearComboBox.setBounds(0, 0, 80, 25);
        dobMonthComboBox.setBounds(90, 0, 100, 25);
        dobDayComboBox.setBounds(200, 0, 50, 25);
        dobPanel.add(dobYearComboBox);
        dobPanel.add(dobMonthComboBox);
        dobPanel.add(dobDayComboBox);

        JLabel msLabel = new JLabel("Start Date:");
        msLabel.setBounds(20, 65, 100, 25);
        JPanel msPanel = new JPanel(null);
        msPanel.setBounds(130, 65, 300, 25);
        msPanel.setBackground(Color.LIGHT_GRAY);
        msYearComboBox.setBounds(0, 0, 80, 25);
        msMonthComboBox.setBounds(90, 0, 100, 25);
        msDayComboBox.setBounds(200, 0, 50, 25);
        msPanel.add(msYearComboBox);
        msPanel.add(msMonthComboBox);
        msPanel.add(msDayComboBox);

        JLabel planLabel = new JLabel("Plan Type:");
        planLabel.setBounds(20, 100, 100, 25);
        planComboBox.setBounds(130, 100, 150, 25);

        JLabel referralLabel = new JLabel("Referral Source:");
        referralLabel.setBounds(20, 135, 100, 25);
        referralSourceField.setBounds(130, 135, 150, 25);

        JLabel trainerLabel = new JLabel("Trainer Name:");
        trainerLabel.setBounds(20, 170, 100, 25);
        trainerNameField.setBounds(130, 170, 150, 25);

        JLabel priceLabel = new JLabel("Regular Price:");
        priceLabel.setBounds(20, 205, 100, 25);
        regularPlanPriceField.setBounds(130, 205, 150, 25);

        JLabel paidAmountLabel = new JLabel("Paid Amount:");
        paidAmountLabel.setBounds(20, 240, 100, 25);
        paidAmountField.setBounds(130, 240, 150, 25);

        JLabel premiumLabel = new JLabel("Premium Price:");
        premiumLabel.setBounds(20, 275, 100, 25);
        premiumPlanChargeField.setBounds(130, 275, 150, 25);

        JLabel discountLabel = new JLabel("Discount:");
        discountLabel.setBounds(20, 310, 100, 25);
        discountAmountField.setBounds(130, 310, 150, 25);

        // Add all components to right panel
        rightPanel.add(dobLabel);
        rightPanel.add(dobPanel);
        rightPanel.add(msLabel);
        rightPanel.add(msPanel);
        rightPanel.add(planLabel);
        rightPanel.add(planComboBox);
        rightPanel.add(referralLabel);
        rightPanel.add(referralSourceField);
        rightPanel.add(trainerLabel);
        rightPanel.add(trainerNameField);
        rightPanel.add(priceLabel);
        rightPanel.add(regularPlanPriceField);
        rightPanel.add(paidAmountLabel);
        rightPanel.add(paidAmountField);
        rightPanel.add(premiumLabel);
        rightPanel.add(premiumPlanChargeField);
        rightPanel.add(discountLabel);
        rightPanel.add(discountAmountField);

        // Button Panel
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(20, 460, 940, 280);
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        mainPanel.add(buttonPanel);
    
        // Add buttons with adjusted layout
        addRegularBtn.setBounds(20, 30, 180, 30);
        buttonPanel.add(addRegularBtn);
    
        addPremiumBtn.setBounds(220, 30, 180, 30);
        buttonPanel.add(addPremiumBtn);
    
        activateBtn.setBounds(420, 30, 180, 30);
        buttonPanel.add(activateBtn);
    
        deactivateBtn.setBounds(620, 30, 180, 30);
        buttonPanel.add(deactivateBtn);
    
        markAttendanceBtn.setBounds(20, 80, 180, 30);
        buttonPanel.add(markAttendanceBtn);
    
        upgradePlanBtn.setBounds(220, 80, 180, 30);
        buttonPanel.add(upgradePlanBtn);
    
        calculateDiscountBtn.setBounds(420, 80, 180, 30);
        buttonPanel.add(calculateDiscountBtn);
    
        payDueBtn.setBounds(620, 80, 180, 30);
        buttonPanel.add(payDueBtn);
    
        revertRegularBtn.setBounds(20, 130, 180, 30);
        buttonPanel.add(revertRegularBtn);
    
        revertPremiumBtn.setBounds(220, 130, 180, 30);
        buttonPanel.add(revertPremiumBtn);
    
        displayBtn.setBounds(420, 130, 180, 30);
        buttonPanel.add(displayBtn);
    
        saveBtn.setBounds(620, 130, 180, 30);
        buttonPanel.add(saveBtn);

        readFileBtn.setBounds(20, 180, 180, 30); // Add below revertRegularBtn
        buttonPanel.add(readFileBtn);
        
        clearBtn.setBounds(320, 180, 180, 30);
        buttonPanel.add(clearBtn);
    }

    private void setupEventHandlers() {
        addRegularBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRegularMember();
            }
        });
        
        addPremiumBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPremiumMember();
            }
        });
        
        activateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activateMembership();
            }
        });
        
        deactivateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deactivateMembership();
            }
        });
        
        markAttendanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAttendance();
            }
        });
        
        upgradePlanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upgradePlan();
            }
        });
        
        calculateDiscountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateDiscount();
            }
        });
        
        payDueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payDueAmount();
            }
        });
        
        revertRegularBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revertMember(true);
            }
        });
        
        revertPremiumBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revertMember(false);
            }
        });
        
        displayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMembers();
            }
        });
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });
        
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        readFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMembersFromFile();
            }
        });
    }

    private boolean isValidPhone(String phone) {
        // Validates 10-digit phone number
        return phone.matches("\\d{10}");
    }

    private boolean isValidEmail(String email) {
        // Basic email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void addRegularMember() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isDuplicateId(id)) {
                JOptionPane.showMessageDialog(this, "Member ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!trainerNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Trainer name is only for Premium Members!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String selectedPlan = (String) planComboBox.getSelectedItem();
            if (selectedPlan.equals("Select Plan")) {
                JOptionPane.showMessageDialog(this, "Please select a plan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double planPrice = Double.parseDouble(regularPlanPriceField.getText());

            String phone = phoneField.getText().trim();
            if (!isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid phone number! Please enter 10 digits.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String email = emailField.getText().trim();
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid email format!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            RegularMember member = new RegularMember(
                id,
                name,
                locationField.getText().trim(),
                phone,
                email,
                getSelectedGender(),
                getSelectedDate(dobYearComboBox, dobMonthComboBox, dobDayComboBox),
                getSelectedDate(msYearComboBox, msMonthComboBox, msDayComboBox),
                selectedPlan,  // Use the selected plan instead of hardcoding
                referralSourceField.getText().trim(),
                planPrice  // Pass the plan price to constructor
            );
            
            membersList.add(member);
            JOptionPane.showMessageDialog(this, "Regular member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPremiumMember() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isDuplicateId(id)) {
                JOptionPane.showMessageDialog(this, "Member ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Add plan type validation
            if (planComboBox.getSelectedIndex() != 0) {
                JOptionPane.showMessageDialog(this, "Plan type is not applicable for Premium Members!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!referralSourceField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Referral source is only for Regular Members!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String phone = phoneField.getText().trim();
            if (!isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid phone number! Please enter 10 digits.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String email = emailField.getText().trim();
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid email format!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            PremiumMember member = new PremiumMember(
                id,
                name,
                locationField.getText().trim(),
                phone,
                email,
                getSelectedGender(),
                getSelectedDate(dobYearComboBox, dobMonthComboBox, dobDayComboBox),
                getSelectedDate(msYearComboBox, msMonthComboBox, msDayComboBox),
                trainerNameField.getText().trim()
            );
            
            membersList.add(member);
            JOptionPane.showMessageDialog(this, "Premium member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void activateMembership() {
        String id = JOptionPane.showInputDialog(this, "Enter Member ID to activate:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GymMember member = findMemberById(Integer.parseInt(id.trim()));
            if (member != null) {
                member.activateMembership();
                JOptionPane.showMessageDialog(this, "Membership activated for ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deactivateMembership() {
        String id = JOptionPane.showInputDialog(this, "Enter Member ID to deactivate:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GymMember member = findMemberById(Integer.parseInt(id.trim()));
            if (member != null) {
                member.deactivateMembership();
                JOptionPane.showMessageDialog(this, "Membership deactivated for ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markAttendance() {
        String id = JOptionPane.showInputDialog(this, "Enter Member ID to mark attendance:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GymMember member = findMemberById(Integer.parseInt(id.trim()));
            if (member == null) {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!member.isActive()) {
                JOptionPane.showMessageDialog(this, "Cannot mark attendance for inactive member!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            member.markAttendance();
            JOptionPane.showMessageDialog(this, 
                "Attendance marked for " + member.getName() + 
                "\nTotal Attendance: " + member.getAttendance() +
                "\nLoyalty Points: " + member.getLoyaltyPoints(), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void upgradePlan() {
        String id = JOptionPane.showInputDialog(this, "Enter Regular Member ID to upgrade plan:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GymMember member = findMemberById(Integer.parseInt(id.trim()));
            if (member == null) {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!(member instanceof RegularMember)) {
                JOptionPane.showMessageDialog(this, "Only regular members can upgrade plans!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!member.isActive()) {
                JOptionPane.showMessageDialog(this, "Cannot upgrade plan for inactive member!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            RegularMember regularMember = (RegularMember) member;
            String newPlan = (String) JOptionPane.showInputDialog(
                this,
                "Select new plan:",
                "Upgrade Plan",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Basic", "Standard", "Deluxe"},
                "Basic"
            );
            
            if (newPlan != null) {
                String result = regularMember.upgradePlan(newPlan);
                JOptionPane.showMessageDialog(this, result, "Upgrade Plan", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateDiscount() {
        String id = JOptionPane.showInputDialog(this, "Enter Premium Member ID to calculate discount:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GymMember member = findMemberById(Integer.parseInt(id.trim()));
            if (member == null) {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!(member instanceof PremiumMember)) {
                JOptionPane.showMessageDialog(this, "Only premium members can calculate discount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            PremiumMember premiumMember = (PremiumMember) member;
            premiumMember.calculateDiscount();
            discountAmountField.setText(String.format("%.2f", premiumMember.getDiscountAmount()));
            JOptionPane.showMessageDialog(this, 
                "Discount calculated: " + premiumMember.getDiscountAmount() + 
                "\nFull Payment: " + (premiumMember.isFullPayment() ? "Yes" : "No"),
                "Discount", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void payDueAmount() {
        String id = JOptionPane.showInputDialog(this, "Enter Premium Member ID to pay due amount:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to pay:");
        if (amountStr == null || amountStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GymMember member = findMemberById(Integer.parseInt(id.trim()));
            if (member == null) {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!(member instanceof PremiumMember)) {
                JOptionPane.showMessageDialog(this, "Only premium members can pay due amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double amount = Double.parseDouble(amountStr.trim());
            PremiumMember premiumMember = (PremiumMember) member;
            String result = premiumMember.payDueAmount(amount);
            JOptionPane.showMessageDialog(this, result, "Payment", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void revertMember(boolean isRegular) {
        String id = JOptionPane.showInputDialog(this, "Enter Member ID to revert:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GymMember member = findMemberById(Integer.parseInt(id.trim()));
            if (member == null) {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String reason = JOptionPane.showInputDialog(this, "Enter removal reason:");
            if (reason == null || reason.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Removal reason cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isRegular && member instanceof RegularMember) {
                RegularMember regMember = (RegularMember) member;
                regMember.attendance = 0;
                regMember.loyaltyPoints = 0;
                regMember.deactivateMembership();
                regMember.setPlan("Basic");
                regMember.setEligibleForUpgrade(false);
                regMember.setRemovalReason(reason.trim());
                JOptionPane.showMessageDialog(this, "Regular member reverted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (!isRegular && member instanceof PremiumMember) {
                PremiumMember premMember = (PremiumMember) member;
                premMember.attendance = 0;
                premMember.loyaltyPoints = 0;
                premMember.deactivateMembership();
                // Remove trainer name and set paid amount to zero
                try {
                    java.lang.reflect.Field trainerField = PremiumMember.class.getDeclaredField("trainerName");
                    trainerField.setAccessible(true);
                    trainerField.set(premMember, "");
                    java.lang.reflect.Field paidField = PremiumMember.class.getDeclaredField("paidAmount");
                    paidField.setAccessible(true);
                    paidField.set(premMember, 0.0);
                    java.lang.reflect.Field isFullPaymentField = PremiumMember.class.getDeclaredField("isFullPayment");
                    isFullPaymentField.setAccessible(true);
                    isFullPaymentField.set(premMember, false);
                    java.lang.reflect.Field discountField = PremiumMember.class.getDeclaredField("discountAmount");
                    discountField.setAccessible(true);
                    discountField.set(premMember, 0.0);
                } catch (Exception ex) {
                    // Should not happen
                }
                JOptionPane.showMessageDialog(this, "Premium member reverted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    isRegular ? "Member is not a regular member!" : "Member is not a premium member!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayMembers() {
        if (membersList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No members to display!", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (GymMember member : membersList) {
            // Skip inactive members
            if (!member.isActive()) {
                continue;
            }

            if (member instanceof PremiumMember) {
                PremiumMember pm = (PremiumMember) member;
                double totalAmount = 50000.00;
                double discount = pm.getDiscountAmount();
                double paidAmount = pm.getPaidAmount();
                double loyaltyDiscount = pm.isFullPayment() ? totalAmount * 0.10 : 0;
                String status = pm.isActive() ? "Active" : "Inactive";
                sb.append(String.format("""
                    ID: %d
                    Name: %s
                    Location: %s
                    Phone: %s
                    Email: %s
                    Gender: %s
                    Date of Birth: %s
                    Membership Start: %s
                    Trainer Name: %s
                    Total Amount: %.2f
                    Loyalty Discount: %.2f
                    Additional Discount: %.2f
                    Amount Paid: %.2f
                    Balance Due: %.2f
                    Status: %s
                    """,
                    pm.getId(), pm.getName(), pm.getLocation(), pm.getPhone(),
                    pm.getEmail(), pm.getGender(), pm.getDOB(),
                    pm.getMembershipStartDate(), pm.getPersonalTrainer(),
                    totalAmount, loyaltyDiscount, discount, paidAmount,
                    totalAmount - loyaltyDiscount - discount - paidAmount,
                    status
                ));
            } else {
                RegularMember rm = (RegularMember) member;
                double planPrice = rm.getPlanPrice(); // Get price directly from RegularMember
                String status = rm.isActive() ? "Active" : "Inactive";
                sb.append(String.format("""
                    ID: %d
                    Name: %s
                    Location: %s
                    Phone: %s
                    Email: %s
                    Gender: %s
                    Date of Birth: %s
                    Membership Start: %s
                    Plan Type: %s
                    Referral Source: %s
                    Total Amount: %.2f
                    Status: %s
                    """,
                    rm.getId(), rm.getName(), rm.getLocation(), rm.getPhone(),
                    rm.getEmail(), rm.getGender(), rm.getDOB(),
                    rm.getMembershipStartDate(), rm.getPlan(), rm.getReferralSource(),
                    planPrice,  // Use the price from RegularMember class
                    status
                ));
            }
            sb.append("\n----------------------------------------\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JDialog dialog = new JDialog(this, "Active Member Details", true);
        dialog.setSize(700, 500);
        dialog.add(scrollPane);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter("MemberDetails.txt")) {
            for (GymMember member : membersList) {
                writer.write(member.toString() + "\n----------------------------------------\n");
            }
            JOptionPane.showMessageDialog(this, "Member details saved to file successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // read from file  
    private void readFromFile() {
        membersList.clear(); // Clear current list before loading
        try (BufferedReader reader = new BufferedReader(new FileReader("MemberDetails.txt"))) {
            String line;
            ArrayList<String> memberBlock = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("----------------------------------------")) {
                    if (!memberBlock.isEmpty()) {
                        parseAndAddMember(memberBlock);
                        memberBlock.clear();
                    }
                } else {
                    memberBlock.add(line);
                }
            }
            // Add last member if file doesn't end with separator
            if (!memberBlock.isEmpty()) {
                parseAndAddMember(memberBlock);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Parses a block of lines representing a member from the text file and adds it to membersList.
     * This method restores all key member fields (attendance, loyalty points, status, etc.)
     * so that loaded members are fully functional in the application.
     *
     * @param block The lines of text representing a single member's details.
     */
    private void parseAndAddMember(ArrayList<String> block) {
        try {
            boolean isPremium = false;
            for (String line : block) {
                if (line.contains("Trainer Name:")) {
                    isPremium = true;
                    break;
                }
            }
            int id = 0;
            String name = "", location = "", phone = "", email = "", gender = "", dob = "", ms = "";
            String plan = "", referral = "", trainer = "", removalReason = "";
            double planPrice = 0, paidAmount = 0;
            int attendance = 0;
            double loyaltyPoints = 0;
            boolean activeStatus = false;
            for (String line : block) {
                if (line.startsWith("ID:")) id = Integer.parseInt(line.split(":",2)[1].trim());
                else if (line.startsWith("Name:")) name = line.split(":",2)[1].trim();
                else if (line.startsWith("Location:")) location = line.split(":",2)[1].trim();
                else if (line.startsWith("Phone:")) phone = line.split(":",2)[1].trim();
                else if (line.startsWith("Email:")) email = line.split(":",2)[1].trim();
                else if (line.startsWith("DOB:")) dob = line.split(":",2)[1].trim();
                else if (line.startsWith("Date of Birth:")) dob = line.split(":",2)[1].trim();
                else if (line.startsWith("Membership Start:")) ms = line.split(":",2)[1].trim();
                else if (line.startsWith("Plan Type:")) plan = line.split(":",2)[1].trim();
                else if (line.startsWith("Plan:")) plan = line.split(":",2)[1].trim();
                else if (line.startsWith("Referral Source:")) referral = line.split(":",2)[1].trim();
                else if (line.startsWith("Price:")) planPrice = Double.parseDouble(line.split(":",2)[1].trim());
                else if (line.startsWith("Total Amount:")) planPrice = Double.parseDouble(line.split(":",2)[1].trim());
                else if (line.startsWith("Trainer Name:")) trainer = line.split(":",2)[1].trim();
                else if (line.startsWith("Amount Paid:")) paidAmount = Double.parseDouble(line.split(":",2)[1].trim());
                else if (line.startsWith("Removal Reason:")) removalReason = line.split(":",2)[1].trim();
                else if (line.startsWith("Attendance:")) attendance = Integer.parseInt(line.split(":",2)[1].trim());
                else if (line.startsWith("Loyalty Points:")) loyaltyPoints = Double.parseDouble(line.split(":",2)[1].trim());
                else if (line.startsWith("Status:")) activeStatus = line.split(":",2)[1].trim().equalsIgnoreCase("Active");
            }
            if (isPremium) {
                PremiumMember pm = new PremiumMember(id, name, location, phone, email, gender, dob, ms, trainer);
                if (activeStatus) pm.activateMembership();
                pm.attendance = attendance;
                pm.loyaltyPoints = loyaltyPoints;
                // Optionally set paidAmount if needed
                membersList.add(pm);
            } else {
                RegularMember rm = new RegularMember(id, name, location, phone, email, gender, dob, ms, plan, referral, planPrice);
                if (activeStatus) rm.activateMembership();
                rm.attendance = attendance;
                rm.loyaltyPoints = loyaltyPoints;
                membersList.add(rm);
            }
        } catch (Exception e) {
            // Ignore malformed member blocks
        }
    }

    /**
     * Reads the contents of MemberDetails.txt and displays them in a dialog.
     */
    private void displayMembersFromFile() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("MemberDetails.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (sb.length() == 0) {
            JOptionPane.showMessageDialog(this, "No saved members to display!", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JDialog dialog = new JDialog(this, "Saved Member Details", true);
        dialog.setSize(700, 500);
        dialog.add(scrollPane);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
        phoneField.setText("");
        emailField.setText("");
        referralSourceField.setText("");
        paidAmountField.setText("");
        removalReasonField.setText("");
        trainerNameField.setText("");
        discountAmountField.setText("0.00");
        
        genderGroup.clearSelection();
        dobYearComboBox.setSelectedIndex(0);
        dobMonthComboBox.setSelectedIndex(0);
        dobDayComboBox.setSelectedIndex(0);
        msYearComboBox.setSelectedIndex(0);
        msMonthComboBox.setSelectedIndex(0);
        msDayComboBox.setSelectedIndex(0);
        planComboBox.setSelectedIndex(0);
    }

    private String getSelectedGender() {
        if (maleRadio.isSelected()) return "Male";
        if (femaleRadio.isSelected()) return "Female";
        if (otherRadio.isSelected()) return "Other";
        return "";
    }

    private String getSelectedDate(JComboBox<String> yearBox, JComboBox<String> monthBox, JComboBox<String> dayBox) {
        return dayBox.getSelectedItem() + " " + monthBox.getSelectedItem() + " " + yearBox.getSelectedItem();
    }

    private boolean isDuplicateId(int id) {
        for (GymMember member : membersList) {
            if (member.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private GymMember findMemberById(int id) {
        for (GymMember member : membersList) {
            if (member.getId() == id) {
                return member;
            }
        }
        JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GymGUI();
            }
        });
    }
}
