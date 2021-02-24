package ua.nure.style.form;

import lombok.Data;

@Data
public class NewBookingForm {
    public String specialFeatures;
    public String startDate;
    public String endDate;
    public String[] service;
    public boolean paid;
}