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

import java.util.LinkedList;
import java.util.List;

public class ABCOrder {

    private OrderHeader header;
    private List<OrderItem> orderItems = new LinkedList<OrderItem>();

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderHeader getHeader() {
        return header;
    }

    public ABCOrder setHeader(OrderHeader header) {
        this.header = header;
        return this;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public ABCOrder addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        return this;
    }
    
    public String toString() {
        String order =
               "============== ABC Order =============\n"
               + "customerNum : " + header.getCustomerNum() + "\n"
               + "status : " + header.getStatus() + "\n"
               + "orderNum : " + header.getOrderNum() + "\n"
               + "============= Line Items =============\n";
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                order += "id : " + item.getId() + "\n"
                        + "price : " + item.getPrice() + "\n"
                        + "quantity : " + item.getQuantity() + "\n";
            }
        }
        
        return order;
    }
}
