package com.example.velotochka.entities;

import javax.persistence.*;

@Entity
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String value;
    private String type;

    @OneToOne
    @JoinColumn(name = "valueProduct", referencedColumnName = "id")
    private Product valueProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getValueProduct() {
        return valueProduct;
    }

    public void setValueProduct(Product valueProduct) {
        this.valueProduct = valueProduct;
    }

    @Transient
    Object getConvertedValue() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException eInt){
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException eDouble){
                return value;
            }
        }

    }
}
