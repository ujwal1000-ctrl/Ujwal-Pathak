package Coursework.GymGUICoursework;

/**
 * Represents a premium gym member with additional features such as personal trainer,
 * premium charge, payment tracking, and discount calculation.
 * <p>
 * This class extends GymMember and provides methods for handling premium-specific
 * operations like payment, discount calculation, and reverting membership.
 * </p>
 */
public class PremiumMember extends GymMember {
    // The fixed charge for premium membership
    private final double premiumCharge = 50000;
    // Name of the assigned personal trainer
    private String trainerName;
    // Indicates if the member has completed full payment
    private boolean isFullPayment;
    // Amount paid by the member
    private double paidAmount;
    // Discount amount applicable to the member
    private double discountAmount;

    public PremiumMember(int id, String name, String location, String phone, String email,
                        String gender, String DOB, String membershipStartDate,
                        String trainerName) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate);
        this.trainerName = trainerName;
        this.isFullPayment = false;
        this.paidAmount = 0;
        this.discountAmount = 0;
    }

    // Accessor methods
    public String getPersonalTrainer() { return trainerName; }
    public boolean isFullPayment() { return isFullPayment; }
    public double getPaidAmount() { return paidAmount; }
    public double getDiscountAmount() { return discountAmount; }

    @Override
    public void markAttendance() {
        if (this.activeStatus) {
            this.attendance++;
            this.loyaltyPoints += 10;
        }
    }

    public String payDueAmount(double amount) {
        if (isFullPayment) {
            return "Payment already completed!";
        }
        
        if (amount <= 0) {
            return "Invalid payment amount!";
        }
        
        paidAmount += amount;
        if (paidAmount >= premiumCharge) {
            isFullPayment = true;
            paidAmount = premiumCharge;
            return "Payment completed!";
        }
        
        return String.format("Payment received. Remaining amount: %.2f", (premiumCharge - paidAmount));
    }

    public void calculateDiscount() {
        if (isFullPayment) {
            discountAmount = 0.1 * premiumCharge;
        } else {
            discountAmount = 0;
        }
    }

    public void revertPremiumMember() {
        this.activeStatus = false;
        this.attendance = 0;
        this.loyaltyPoints = 0;
        this.trainerName = "";
        this.isFullPayment = false;
        this.paidAmount = 0;
        this.discountAmount = 0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
            "\nTrainer Name: %s\nPaid Amount: %.2f\nFull Payment: %s\nDiscount Amount: %.2f\nRemaining Amount: %.2f",
            trainerName, paidAmount, isFullPayment ? "Yes" : "No", discountAmount, (premiumCharge - paidAmount)
        );
    }
}
