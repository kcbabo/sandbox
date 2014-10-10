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

public class OrderHeader {

    private String customerNum;
    private Status status;
    private String orderNum;
    
    public String getCustomerNum() {
        return customerNum;
    }
    
    public OrderHeader setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
        return this;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public OrderHeader setStatus(Status status) {
        this.status = status;
        return this;
    }
    
    public String getOrderNum() {
        return orderNum;
    }
    
    public OrderHeader setOrderNum(String orderNum) {
        this.orderNum = orderNum;
        return this;
    }
    
}
