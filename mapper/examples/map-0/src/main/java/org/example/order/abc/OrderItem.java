/*
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.order.abc;

public class OrderItem {

    private String id;
    private double price;
    private int quantity;
    
    public String getId() {
        return id;
    }
    
    public OrderItem setId(String id) {
        this.id = id;
        return this;
    }
    
    public double getPrice() {
        return price;
    }
    
    public OrderItem setPrice(double price) {
        this.price = price;
        return this;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public OrderItem setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
    
}
