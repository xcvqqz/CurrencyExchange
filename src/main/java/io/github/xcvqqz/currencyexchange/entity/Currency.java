package io.github.xcvqqz.currencyexchange.entity;

public class Currency {

    private int id;
    private String code;
    private String name;
    private String sign;

    public Currency(int id, String name, String code, String sign) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public Currency(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getCode() {return code;}
    public void setCode(String code) {this.code = code;}
    public String getName() {return name;}
    public String getSign() {return sign;}
}