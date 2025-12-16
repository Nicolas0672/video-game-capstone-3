package org.yearup.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Product
{
    @Positive(message = "productId needs to be positive")
    private int productId;

    @NotBlank(message = "Name is required")
    private String name;

    @Positive(message = "Price needs to be positive")
    private BigDecimal price;

    @Positive(message = "CategoryId needs to be positive")
    private int categoryId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Subcategory is required")
    private String subCategory;

    @Positive(message = "Stock needs to be positive")
    private int stock;

    @NotNull(message = "isFeatured is required")
    private Boolean isFeatured;

    @NotBlank(message = "ImageUrl is required")
    private String imageUrl;

    public Product()
    {
    }

    public Product(int productId, String name, BigDecimal price, int categoryId, String description, String subCategory, int stock, boolean isFeatured, String imageUrl)
    {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.description = description;
        this.subCategory = subCategory;
        this.stock = stock;
        this.isFeatured = isFeatured;
        this.imageUrl = imageUrl;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory(String subCategory)
    {
        this.subCategory = subCategory;
    }

    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }

    public boolean isFeatured()
    {
        return isFeatured;
    }

    public void setFeatured(boolean featured)
    {
        isFeatured = featured;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }
}
