package Coursework.GymGUICoursework;

/**
 * Abstract base class representing a gym member.
 * <p>
 * This class defines common fields and methods for all types of gym members,
 * such as RegularMember and PremiumMember. It provides accessor methods,
 * membership activation/deactivation, attendance tracking, and a string
 * representation of member details. Subclasses must implement markAttendance().
 * </p>
 */
abstract class GymMember {
    // Tracks the number of times the member has attended the gym
    protected int attendance;
    // Indicates if the membership is currently active
    protected boolean activeStatus;
    // Loyalty points accumulated by the member
    protected double loyaltyPoints;

    protected int id;
    protected String DOB;
    protected String name;
    protected String location;
    protected String phone;
    protected String email;
    protected String gender;
    protected String membershipStartDate;

    public GymMember(int id, String name, String location, String phone, String email, 
                    String gender, String DOB, String membershipStartDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.DOB = DOB;
        this.membershipStartDate = membershipStartDate;
        this.attendance = 0;
        this.loyaltyPoints = 0;
        this.activeStatus = false;
    }

    // Accessor methods
    public int getId() { return id; }
    public String getDOB() { return DOB; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getMembershipStartDate() { return membershipStartDate; }
    public int getAttendance() { return attendance; }
    public double getLoyaltyPoints() { return loyaltyPoints; }
    public boolean isActive() { return activeStatus; }

    // Member operations
    public void activateMembership() {
        this.activeStatus = true;
    }

    public void deactivateMembership() {
        this.activeStatus = false;
    }

    public abstract void markAttendance();

    public void resetMember() {
        this.activeStatus = false;
        this.attendance = 0;
        this.loyaltyPoints = 0;
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %d\nName: %s\nLocation: %s\nPhone: %s\nEmail: %s\nGender: %s\n" +
            "DOB: %s\nMembership Start: %s\nAttendance: %d\nLoyalty Points: %.2f\n" +
            "Status: %s",
            id, name, location, phone, email, gender, DOB, membershipStartDate,
            attendance, loyaltyPoints, activeStatus ? "Active" : "Inactive"
        );
    }
}
