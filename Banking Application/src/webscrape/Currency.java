package webscrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.text.DecimalFormat;

public class Currency {
    //declare fields
    private final String[] CODES = {
    "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF",
    "BMD", "BND", "BOB", "BOV", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHE", "CHF", "CHW", "CLF",
    "CLP", "CNY", "COP", "COU", "CRC", "CUC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB",
    "EUR", "FJD", "FKP", "GBP", "GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF",
    "IDR", "ILS", "INR", "IQD", "IRR", "ISK", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD",
    "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO",
    "MUR", "MVR", "MWK", "MXN", "MXV", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN",
    "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD",
    "SHP", "SLL", "SOS", "SRD", "SSP", "STD", "SVC", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD",
    "TWD", "TZS", "UAH", "UGX", "USD", "USN", "UYI", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XAG", "XAU",
    "XCD", "XDR", "XOF", "XPD", "XPF", "XPT", "XSU", "XTS", "XUA", "YER", "ZAR", "ZMW"
    };
    private String fromCurrency, toCurrency;
    private String url;

    //overiding constructor
    public Currency() {
        this.fromCurrency = "";
        this.toCurrency = "";
    }

    //overloading constructor
    public Currency(String fromCurrency, String toCurrency) {
        this.fromCurrency = fromCurrency.toUpperCase();
        this.toCurrency = toCurrency.toUpperCase();
    }

    //set the currency being converted
    public void setFromCurrency(String code) {
        this.fromCurrency = code.toUpperCase();
    }

    //set the currency that's being converted to 
    public void setToCurrency(String code) {
        this.toCurrency = code.toUpperCase();
    }

    //get the currency being converted
    public String getFromCurrency() {
        return this.fromCurrency;
    }

    //get the currency that's being converted to
    public String getToCurrency() {
        return this.toCurrency;
    }

    //return boolean if code isn't valid
    public boolean notCode(String code) {
        boolean notCode = true;
        for (String i : CODES) {
            if (code.equalsIgnoreCase(i))
                notCode = false;
        }

        return notCode;
    }
    
    //webscrape the current rate of conversion from google
    public double getRate() {
        //initialize variables
        double dataValue = 0;
        //create a url to search the conversion between the two currencies
        url = String.format("https://www.google.com/search?q=%s+to+%s", fromCurrency, toCurrency);

        try {
            //connects to the url and assigns the HTML document to variable
            Document document = Jsoup.connect(url).get();
            //assign the variable with the first element from the HTML document that matches the CSS selector
            Element spanElement = document.selectFirst("span.DFlfde.SwHCTb");
            //takes the value data-value(conversion rate) from spanElement as a double value
            dataValue = Double.parseDouble(spanElement.attr("data-value"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //return the conversion rate
        return dataValue;
    }

    //returns a neatly formatted conversion and calculation
    public String toString(double amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        String str = "";
        double rate = getRate();
        amount = Double.parseDouble(df.format(amount));
        
        str += String.format("1 %s  =>  %s %s\n", fromCurrency, df.format(rate), toCurrency);
        str += String.format("%s %s  =>  %s %s", df.format(amount), fromCurrency, df.format(amount * rate), toCurrency);

        return str;
    }

}
