package Coursework.GymGUICoursework;

/**
 * Represents a regular gym member with plan options, referral source, and upgrade eligibility.
 * <p>
 * This class extends GymMember and provides additional fields and methods for handling
 * regular membership plans, plan upgrades, and referral tracking. It manages attendance
 * limits for upgrades and supports reverting membership status.
 * </p>
 */
public class RegularMember extends GymMember {
    // Maximum allowed attendances before eligible for upgrade
    private final int attendanceLimit = 30;
    // Indicates if the member is eligible for a plan upgrade
    private boolean isEligibleForUpgrade;
    // Reason for member removal
    private String removalReason;
    // Source from which the member was referred
    private String referralSource;
    // Current plan type of the member
    private String plan;
    // Price of the current plan
    private double planPrice;  // Updated field

    // Updated constructor to include planPrice
    public RegularMember(int id, String name, String location, String phone, String email,
                        String gender, String DOB, String membershipStartDate,
                        String plan, String referralSource, double planPrice) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate);
        this.referralSource = referralSource;
        this.isEligibleForUpgrade = false;
        this.plan = plan;
        this.planPrice = planPrice;
        this.removalReason = "";
    }

    // Accessor methods
    public boolean isEligibleForUpgrade() { return isEligibleForUpgrade; }
    public String getRemovalReason() { return removalReason; }
    public String getReferralSource() { return referralSource; }
    public String getPlan() { return plan; }
    public double getPlanPrice() { return planPrice; }  // Updated getter

    // Add public setter for removalReason
    public void setRemovalReason(String reason) {
        this.removalReason = reason;
    }

    // Add public setter for isEligibleForUpgrade
    public void setEligibleForUpgrade(boolean eligible) {
        this.isEligibleForUpgrade = eligible;
    }

    @Override
    public void markAttendance() {
        if (this.activeStatus) {
            this.attendance++;
            this.loyaltyPoints += 5;
            if (this.attendance >= attendanceLimit) {
                this.isEligibleForUpgrade = true;
            }
        }
    }

    // Updated setPlan method
    public void setPlan(String plan) {
        this.plan = plan;
        // Update price based on plan
        switch (plan) {
            case "Standard":
                this.planPrice = 12000.00;
                break;
            case "Deluxe":
                this.planPrice = 15000.00;
                break;
            default:  
                this.planPrice = 0.00;
        }
    }

    public String upgradePlan(String newPlan) {
        if (!isEligibleForUpgrade) {
            return "Not eligible for upgrade. Need " + (attendanceLimit - attendance) + " more attendances.";
        }
        
        double newPrice = getPlanPrice();
        if (newPrice == -1) return "Invalid plan selected!";
        if (plan.equalsIgnoreCase(newPlan)) return "Already subscribed to " + plan + " plan.";
        
        setPlan(newPlan);  // Updated to use setPlan method
        return "Plan upgraded to " + newPlan + " successfully!";
    }

    public void revertRegularMember(String removalReason) {
        super.resetMember();
        this.isEligibleForUpgrade = false;
        this.setPlan("Basic");  // Updated to use setPlan method
        this.removalReason = removalReason;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
            "\nReferral Source: %s\nPlan: %s\nPrice: %.2f\nRemoval Reason: %s",
            referralSource, plan, planPrice, removalReason
        );
    }
}
