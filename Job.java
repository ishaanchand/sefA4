import java.io.*;
import java.util.regex.Pattern;

public class Job {
    private String jobNumber;
    private String jobTitle;
    private String jobPosterName;
    private String jobPosterAddress;
    private String jobPostedDate;
    private String jobExperienceLevel;
    private String jobType;
    private String[] jobRequiredSkills;
    private int jobSalary;
    private String jobDescription;

    public Job(String jobNumber, String jobTitle, String jobPosterName, String jobPosterAddress, String jobPostedDate,
            String jobExperienceLevel, String jobType, String[] jobRequiredSkills, int jobSalary,
            String jobDescription) {
        this.jobNumber = jobNumber;
        this.jobTitle = jobTitle;
        this.jobPosterName = jobPosterName;
        this.jobPosterAddress = jobPosterAddress;
        this.jobPostedDate = jobPostedDate;
        this.jobExperienceLevel = jobExperienceLevel;
        this.jobType = jobType;
        this.jobRequiredSkills = jobRequiredSkills;
        this.jobSalary = jobSalary;
        this.jobDescription = jobDescription;
    }

    public boolean addJob() {
        if (!areAllFieldsFilled()) {
            System.out.println("Error adding job: All fields must be filled.");
            return false;
        }

        if (!isJobNumberValid(jobNumber)) {
            System.out.println("Error adding job: Invalid job number format.");
            return false;
        }

        if (doesJobExist(jobNumber)) {
            System.out.println("Error adding job: Job with the same number already exists.");
            return false;
        }

        if (!isDateValid(jobPostedDate)) {
            System.out.println("Error adding job: Invalid date format.");
            return false;
        }

        if (!isAddressValid(jobPosterAddress)) {
            System.out.println("Error adding job: Invalid address format.");
            return false;
        }

        if (!isSalaryValid()) {
            System.out.println("Error adding job: Invalid salary for the job level.");
            return false;
        }

        if (!isJobTypeValid()) {
            System.out.println("Error adding job: Invalid job type for the job level.");
            return false;
        }

        if (!isSkillsValid()) {
            System.out.println("Error adding job: Invalid skills format.");
            return false;
        }

        if (jobSalary <= 0) {
            System.out.println("Error adding job: Salary must be greater than 0.");
            return false;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("jobs.txt", true));
            writer.write(jobNumber + "," + jobTitle + "," + jobPosterName + "," + jobPosterAddress + ","
                    + jobPostedDate + "," + jobExperienceLevel + "," + jobType + ","
                    + String.join(";", jobRequiredSkills) + "," + jobSalary + "," + jobDescription + "\n");
            writer.close();

            System.out.println("Job added successfully.");
            return true;

        } catch (IOException e) {
            System.out.println("Error adding job: " + e.getMessage());
            return false;
        }
    }

    private boolean doesJobExist(String jobNumber2) {
        return false;
    }

    public boolean updateJob() {
        if (!areAllFieldsFilled()) {
            System.out.println("Error updating job: All fields must be filled.");
            return false;
        }

        if (!isJobNumberValid(jobNumber)) {
            System.out.println("Error updating job: Invalid job number format.");
            return false;
        }

        if (!doesJobExist(jobNumber)) {
            System.out.println("Error updating job: Job does not exist.");
            return false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"));
            StringBuilder jobData = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(jobNumber + ",")) {
                    jobData.append(jobNumber).append(",").append(jobTitle).append(",").append(jobPosterName)
                            .append(",").append(jobPosterAddress).append(",").append(jobPostedDate).append(",")
                            .append(jobExperienceLevel).append(",").append(jobType).append(",")
                            .append(String.join(";", jobRequiredSkills)).append(",").append(jobSalary).append(",")
                            .append(jobDescription).append("\n");
                } else {
                    jobData.append(line).append("\n");
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("jobs.txt"));
            writer.write(jobData.toString());
            writer.close();

            System.out.println("Job updated successfully.");
            return true;

        } catch (IOException e) {
            System.out.println("Error updating job: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteJob() {
        if (!isJobNumberValid(jobNumber)) {
            System.out.println("Error deleting job: Invalid job number format.");
            return false;
        }

        if (!doesJobExist(jobNumber)) {
            System.out.println("Error deleting job: Job does not exist.");
            return false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"));
            StringBuilder jobData = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(jobNumber + ",")) {
                    jobData.append(line).append("\n");
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("jobs.txt"));
            writer.write(jobData.toString());
            writer.close();

            System.out.println("Job deleted successfully.");
            return true;

        } catch (IOException e) {
            System.out.println("Error deleting job: " + e.getMessage());
            return false;
        }
    }

    private boolean isJobNumberValid(String jobNumber) {
        // Condition 1: Job number must start with alphanumeric characters and end with an underscore.
        return Pattern.matches("^[a-zA-Z0-9]+_$", jobNumber);
    }

    private boolean isDateValid(String date) {
        // Condition 2: Date must be in the format "YYYY-MM-DD".
        return Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", date);
    }

    private boolean isAddressValid(String address) {
        // Condition 3: Address must contain at least one alphanumeric character followed by a comma and a space.
        return Pattern.matches("^.*\\w+,\\s.*$", address);
    }

    private boolean isSalaryValid() {
        // Condition 4: Salary must be greater than or equal to 30000 for Junior level, 50000 for Mid-level, and 80000 for Senior level.
        if (jobExperienceLevel.equals("Junior") && jobSalary < 30000) {
            return false;
        } else if (jobExperienceLevel.equals("Mid-level") && jobSalary < 50000) {
            return false;
        } else if (jobExperienceLevel.equals("Senior") && jobSalary < 80000) {
            return false;
        }
        return true;
    }

    private boolean isJobTypeValid() {
        // Condition 5: Job type must be "Full-time" or "Part-time" for Junior and Mid-level jobs.
        // For Senior and Executive jobs, it can also be "Contract" or "Freelance".
        if (jobExperienceLevel.equals("Junior") || jobExperienceLevel.equals("Mid-level")) {
            return jobType.equals("Full-time") || jobType.equals("Part-time");
        }
        return true;
    }

    private boolean isSkillsValid() {
        // Condition 6: Skills must be non-empty and contain only alphanumeric characters, spaces, and commas.
        if (jobRequiredSkills.length == 0) {
            return false;
        }
        for (String skill : jobRequiredSkills) {
            if (!Pattern.matches("^[a-zA-Z0-9\\s,]+$", skill)) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllFieldsFilled() {
        return !jobNumber.isEmpty() && !jobTitle.isEmpty() && !jobPosterName.isEmpty() && !jobPosterAddress.isEmpty()
                && !jobPostedDate.isEmpty() && !jobExperienceLevel.isEmpty() && !jobType.isEmpty()
                && jobRequiredSkills.length > 0 && !jobDescription.isEmpty();
    }

    public static void main(String[] args) {
        Job job = new Job("12345MMM_", "Software Engineer", "Ishaan Chand", "123 Main St, City, State", "2023-05-30",
                "Junior", "Full-time", new String[]{"Java", "Python"}, 40000,
                "We are seeking a skilled Software Engineer to join our development team.");

        job.addJob();
        job.updateJob();
        job.deleteJob();
    }
}