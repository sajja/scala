package com.example.interview;

import java.util.List;
/*
public class InvoiceService {
    private String type = new String("CREDIT");

    private static int mailsSent = 0;

    private mailServer MailServer;

    public InvoiceService(String host, int port) throws MailServerException {
        MailServer = new mailServer(host, port); //connects to remote mail server
    }

    public void printInvoiceTotal(Invoice invoice) {
        double Total = 0;

        //Iterates the invoice rows
        //calculate the total value
        for (Object row : invoice.getInvoiceRows().getRows()) {
            InvoiceRow ir = (InvoiceRow) row;
            double rowValue = ir.getCost() - ir.getDiscount();
            double totalRowValue = rowValue + (rowValue * ir.getVat()) / 100;
            Total += totalRowValue;
        }

        double shippingCost = 0;

        try {
            ShippingMethod shipping = invoice.getShippingMethod();
            int t = shipping.getType();
            switch (t) {
                case 1: //ARAMAX
                    shippingCost = 40 + (invoice.getWeight() * 0.4);
                    break;
                case 2: //FEDEX
//                    shippingCost =….//some other fancy way of calculating cost.
                    break;
                case 3:
                    shippingCost = 20;
                    break;
                default:
                    throw new Exception("invalid shipping method");
            }

        } catch (Exception ex) {
        }

        MailServer.sendMail(invoice.getRecipient().getEmail(), "data".getBytes(), Total);
        MailServer.mailsSent++;

        for (Object row : invoice.getInvoiceRows().getRows()) {
            InvoiceRow ir = (InvoiceRow) row;
            double rowValue = ir.getCost() - ir.getDiscount();
            double totalRowValue = rowValue + (rowValue * ir.getVat()) / 100;
            System.out.println("Row total " + totalRowValue);
        }

        System.out.println("Total shipping cost is for invoice" + type + " is " + (Total + shippingCost));
    }
}

class InvoiceRowList {
    private List rows;

    public List getRows() {
        return rows;
    }

    public void setInvoiceRows(List rows) {
        this.rows = rows;
    }
}

class mailServer {
    private String host;
    private int port;
    public int mailsSent = 0;

    public mailServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void stop() {
    }

    public void sendMail(String email, byte[] payload, int total) {
    }
}


class InvoiceRow {
    private int cost;
    private int discount;
    private int vat;

    public int getVat() {
        return vat;
    }


    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}

class Invoice {
    public InvoiceRowList invoiceRows;
    protected ShippingMethod shippingMethod;
    private Recipient recipient;

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public InvoiceRowList getInvoiceRows() {
        return invoiceRows;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public int getWeight() {
        //calculate
        int calculate = 1 * 10 + 2;//in real life this would be a real calculation
        return calculate;
    }
}

class Recipient {
    public String getEmail() {
        return "john@doe.com";
    }
}

class ShippingMethod {
    public int getType() {
        return 1;
    }
}

class MailServerException extends Exception {
}

*/
