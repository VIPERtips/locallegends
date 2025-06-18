package com.blessify.locallegends.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmailSender {
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    // Constants
    private static final String SUPPORT_EMAIL = "info@byteassassin.co.zw";
    private static final String APP_NAME = "HypeZone";
    private static final String BASE_URL = "https://hypezone.byteassassin.co.zw";
    private static final String ADMIN_EMAIL = "tadiwachipungu2@gmail.com";
    
    // Email Templates Cache
    private String emailTemplate;
    
    /**
     * Core email sending method with builder pattern support
     */
    private void sendEmail(EmailBuilder builder) {
        try {
            String htmlTemplate = getEmailTemplate();
            String htmlBody = processTemplate(htmlTemplate, builder);
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(new InternetAddress(SUPPORT_EMAIL, APP_NAME));
            helper.setTo(builder.toEmail);
            helper.setSubject(builder.subject);
            helper.setText(htmlBody, true);
            
            javaMailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Failed to send email to " + builder.toEmail + ": " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
    
    
    public void sendWelcomeEmail(String userEmail, String username) {
        EmailBuilder builder = new EmailBuilder()
            .to(userEmail)
            .subject("🔥 Welcome to HypeZone - Discover Amazing Local Businesses!")
            .greeting("Hey " + username + "! 👋")
            .message("Welcome to HypeZone, your ultimate destination for discovering amazing local businesses! " +
                    "Your account is now active and ready to explore. Connect with your community, find trending spots, " +
                    "and help support local businesses that make your city unique.")
            .cta("Start Exploring", BASE_URL + "/businesses")
            .features(new String[]{
                "🌟 Discover trending local businesses and hidden gems in your area",
                "⭐ Share reviews and help others find great places",
                "🏆 Build your reputation as a trusted community reviewer",
                "📍 Find businesses by location, category, and ratings"
            })
            .footerMessage("Ready to discover what makes your community special? Let's get started!");
            
        sendEmail(builder);
    }
    
    
    public void sendAdminNotification(String userEmail, String username) {
        EmailBuilder builder = new EmailBuilder()
            .to(ADMIN_EMAIL)
            .subject("🚀 New HypeZone Member Registration")
            .greeting("New Member Alert! 📢")
            .message("A new member has successfully joined the HypeZone community. Please review their account " +
                    "and ensure they receive a warm welcome to help build our growing community.")
            .cta("Review New Member", BASE_URL + "/admin/users")
            .infoBox("Member Registration Details", 
                "Username: " + username + "<br>" +
                "Email: " + userEmail + "<br>" +
                "Registration Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm")) + "<br>" +
                "Status: Active")
            .footerMessage("Let's continue building an amazing community together!");
            
        sendEmail(builder);
    }


    public void sendAdminNotificationNewClaim(String userEmail, String businessName) {
        EmailBuilder builder = new EmailBuilder()
            .to(ADMIN_EMAIL)
            .subject("🏢 New Business Ownership Claim Submitted")
            .greeting("Business Claim Alert! 📋")
            .message("A business owner has submitted a claim to verify ownership of their business listing. " +
                    "Please review the claim details and supporting documentation to process this request.")
            .cta("Review Claim", BASE_URL + "/admin/claims")
            .infoBox("Claim Submission Details", 
                "Business Name: " + businessName + "<br>" +
                "Claimed By: " + userEmail + "<br>" +
                "Submission Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm")) + "<br>" +
                "Status: Pending Review")
            .footerMessage("Thank you for maintaining the quality of our business directory!");
            
        sendEmail(builder);
    }
    
    
    public void sendClaimApprovalEmail(String userEmail, String username, String businessName) {
        EmailBuilder builder = new EmailBuilder()
            .to(userEmail)
            .subject("🎉 Business Ownership Claim Approved!")
            .greeting("Congratulations " + username + "! 🎊")
            .message("Excellent news! Your ownership claim for <strong>" + businessName + "</strong> has been approved. " +
                    "You now have full access to manage your business profile, engage with customers, and showcase " +
                    "what makes your business special to the HypeZone community.")
            .cta("Access Business Dashboard", BASE_URL + "/business/dashboard")
            .infoBox("What You Can Do Now", 
                "• Update your business information, hours, and contact details<br>" +
                "• Upload photos to showcase your business<br>" +
                "• Respond professionally to customer reviews<br>" +
                "• Create special offers and promotions<br>" +
                "• Track your business performance and analytics<br>" +
                "• Connect directly with your customers")
            .footerMessage("Ready to take your business presence to the next level? Let's make it happen!");
            
        sendEmail(builder);
    }
    
    
    public void sendClaimRejectionEmail(String userEmail, String username, String businessName, String reason) {
        EmailBuilder builder = new EmailBuilder()
            .to(userEmail)
            .subject("📋 Business Claim Requires Additional Information")
            .greeting("Hi " + username + ",")
            .message("Thank you for submitting your ownership claim for <strong>" + businessName + "</strong>. " +
                    "To complete the verification process, we need some additional information or documentation. " +
                    "This extra step helps us maintain the integrity of our business directory and protect all business owners.")
            .cta("Update Your Claim", BASE_URL + "/businesses")
            .infoBox("Additional Information Needed", reason)
            .features(new String[]{
                "📄 Ensure all required documents are clear, current, and properly formatted",
                "🏢 Verify that business registration details match your submitted information exactly",
                "📞 Double-check that all contact information is accurate and up-to-date",
                "📍 Confirm that business address and location details are precise",
                "🆔 Provide additional identification or authorization documents if requested"
            })
            .footerMessage("Need help with your claim? Our support team is here to assist you every step of the way!");
            
        sendEmail(builder);
    }
    
   
    public void sendAccountSuspensionEmail(String userEmail, String username, String reason, int suspensionDays) {
        EmailBuilder builder = new EmailBuilder()
            .to(userEmail)
            .subject("⚠️ HypeZone Account Temporarily Suspended")
            .greeting("Hi " + username + ",")
            .message("We're writing to inform you that your HypeZone account has been temporarily suspended for " + 
                    suspensionDays + " day" + (suspensionDays > 1 ? "s" : "") + " due to a violation of our community guidelines. " +
                    "We take community safety and respectful interaction seriously, and this temporary measure helps maintain " +
                    "a positive environment for all our users.")
            .cta("Review Community Guidelines", BASE_URL + "/guidelines")
            .infoBox("Suspension Details", 
                "Reason: " + reason + "<br>" +
                "Duration: " + suspensionDays + " day" + (suspensionDays > 1 ? "s" : "") + "<br>" +
                "Suspension Start: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")) + "<br>" +
                "Account Reinstatement: " + java.time.LocalDateTime.now().plusDays(suspensionDays).format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")) + "<br>" +
                "Status: Automatic reinstatement after suspension period")
            .features(new String[]{
                "📖 Please review our community guidelines during this time",
                "🤝 Consider how to contribute positively to our community",
                "📞 Contact support if you have questions about this decision",
                "⏰ Your account will be automatically reactivated after the suspension period"
            })
            .footerMessage("We value you as a community member and look forward to welcoming you back!");
            
        sendEmail(builder);
    }
    
    
    public void sendNewReviewEmail(String businessOwnerEmail, String businessName, String reviewerName, 
                                  int rating, String reviewText) {
        String ratingStars = "⭐".repeat(rating) + "☆".repeat(Math.max(0, 5 - rating));
        String ratingMessage = rating >= 4 ? "fantastic" : rating >= 3 ? "good" : "constructive";
        
        EmailBuilder builder = new EmailBuilder()
            .to(businessOwnerEmail)
            .subject("🌟 New " + rating + "-Star Review for " + businessName)
            .greeting("You've received a new review! 📝")
            .message("Great news! A customer has shared their experience with <strong>" + businessName + "</strong> " +
                    "by leaving a " + ratingMessage + " " + rating + "-star review. Customer feedback is invaluable " +
                    "for growing your business, building trust, and improving your services.")
            .cta("View Review & Respond", BASE_URL + "/business/reviews")
            .infoBox("Review Details", 
                "Rating: " + ratingStars + " (" + rating + " out of 5 stars)<br>" +
                "Reviewer: " + reviewerName + "<br>" +
                "Review: \"" + (reviewText.length() > 150 ? reviewText.substring(0, 150) + "..." : reviewText) + "\"<br>" +
                "Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")))
            .features(new String[]{
                "💬 Respond professionally to build stronger customer relationships",
                "📈 Monitor review trends to identify areas for improvement",
                "🏆 Share positive reviews on your social media and marketing materials",
                "🔍 Use customer feedback to enhance your service quality",
                "🤝 Thank reviewers for taking time to share their experience"
            })
            .footerMessage("Every review is an opportunity to connect with your customers and grow your business!");
            
        sendEmail(builder);
    }
    
    public void sendReviewsSummaryEmail(String businessOwnerEmail, String businessName, int newReviewsCount, 
                                       double averageRating, int totalReviews) {
        String ratingStars = "⭐".repeat((int)Math.round(averageRating));
        if (averageRating % 1 >= 0.5 && (int)averageRating < 5) {
            ratingStars += "⭐";
        }
        while (ratingStars.length() < 5) {
            ratingStars += "☆";
        }
        
        EmailBuilder builder = new EmailBuilder()
            .to(businessOwnerEmail)
            .subject("📊 Weekly Review Summary for " + businessName)
            .greeting("Your weekly business insights are here! 📈")
            .message("Here's your weekly review summary for <strong>" + businessName + "</strong>. " +
                    "You've received <strong>" + newReviewsCount + "</strong> new review" + 
                    (newReviewsCount != 1 ? "s" : "") + " this week! " +
                    "Your business continues to generate valuable engagement and feedback from the community.")
            .cta("View Detailed Analytics", BASE_URL + "/business/reviews")
            .infoBox("Weekly Review Analytics", 
                "New Reviews This Week: " + newReviewsCount + "<br>" +
                "Current Average Rating: " + ratingStars + " (" + String.format("%.1f", averageRating) + " out of 5)<br>" +
                "Total Reviews: " + totalReviews + "<br>" +
                "Review Response Rate: Visit dashboard for details")
            .features(new String[]{
                "📊 Track your rating trends and improvement over time",
                "💡 Identify common themes in customer feedback",
                "🎯 Set goals for customer satisfaction and service quality",
                "📢 Use your excellent ratings in marketing and promotions",
                "🔄 Implement changes based on constructive feedback"
            })
            .footerMessage("Keep up the excellent work in serving your customers!");
            
        sendEmail(builder);
    }
    
    
    private String getEmailTemplate() throws IOException {
        if (emailTemplate == null) {
            Path templatePath = new ClassPathResource("templates/email-template.html").getFile().toPath();
            emailTemplate = Files.readString(templatePath, StandardCharsets.UTF_8);
        }
        return emailTemplate;
    }
    
    
    private String processTemplate(String template, EmailBuilder builder) {
        String processed = template
            .replace("{{EMAIL_TITLE}}", builder.subject)
            .replace("{{GREETING}}", builder.greeting)
            .replace("{{MESSAGE_CONTENT}}", builder.message)
            .replace("{{UNSUBSCRIBE_URL}}", BASE_URL + "/unsubscribe")
            .replace("{{PRIVACY_URL}}", BASE_URL + "/privacy")
            .replace("{{SUPPORT_URL}}", BASE_URL + "/contact")
            .replace("{{USER_EMAIL}}", builder.toEmail)
            .replace("{{FOOTER_MESSAGE}}", builder.footerMessage);
        
        // Process CTA
        if (builder.ctaText != null && builder.ctaUrl != null) {
            processed = processed
                .replace("{{CTA_URL}}", builder.ctaUrl)
                .replace("{{CTA_TEXT}}", builder.ctaText)
                .replace("{{#CTA_BUTTON}}", "")
                .replace("{{/CTA_BUTTON}}", "");
        } else {
            processed = processed.replaceAll("(?s)\\{\\{#CTA_BUTTON\\}\\}.*?\\{\\{/CTA_BUTTON\\}\\}", "");
        }
        
        
        if (builder.infoTitle != null && builder.infoContent != null) {
            processed = processed
                .replace("{{INFO_TITLE}}", builder.infoTitle)
                .replace("{{INFO_CONTENT}}", builder.infoContent)
                .replace("{{#INFO_BOX}}", "")
                .replace("{{/INFO_BOX}}", "");
        } else {
            processed = processed.replaceAll("(?s)\\{\\{#INFO_BOX\\}\\}.*?\\{\\{/INFO_BOX\\}\\}", "");
        }
        
       
        if (builder.features != null && builder.features.length > 0) {
            StringBuilder featuresBuilder = new StringBuilder();
            for (String item : builder.features) {
                featuresBuilder.append("<li>").append(item).append("</li>");
            }
            processed = processed
                .replace("{{#FEATURES}}", "")
                .replace("{{/FEATURES}}", "")
                .replace("{{#FEATURE_ITEMS}}", "")
                .replace("{{/FEATURE_ITEMS}}", "")
                .replace("<ul class=\"features-list\">", "<ul class=\"features-list\">" + featuresBuilder.toString());
        } else {
            processed = processed.replaceAll("(?s)\\{\\{#FEATURES\\}\\}.*?\\{\\{/FEATURES\\}\\}", "");
        }
        
        return processed;
    }
    
   
    private static class EmailBuilder {
        private String toEmail;
        private String subject;
        private String greeting;
        private String message;
        private String ctaText;
        private String ctaUrl;
        private String infoTitle;
        private String infoContent;
        private String[] features;
        private String footerMessage = "Thanks for being part of the HypeZone community!";
        
        public EmailBuilder to(String email) { 
            this.toEmail = email; 
            return this; }
        public EmailBuilder subject(String subject) { 
            this.subject = subject;
            return this; }
        public EmailBuilder greeting(String greeting) { 
            this.greeting = greeting; 
            return this; }
        public EmailBuilder message(String message) { 
            this.message = message; 
            return this; }
        public EmailBuilder cta(String text, String url) { 
            this.ctaText = text; 
            this.ctaUrl = url; 
            return this; }
        public EmailBuilder infoBox(String title, String content) { 
            this.infoTitle = title; 
            this.infoContent = content; 
            return this; }
        public EmailBuilder features(String[] features) { 
            this.features = features; 
            return this; }
        public EmailBuilder footerMessage(String message) { 
            this.footerMessage = message; 
            return this; }
    }
}