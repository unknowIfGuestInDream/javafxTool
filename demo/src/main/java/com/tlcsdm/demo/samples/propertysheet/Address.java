/**
 * Copyright (c) 2014, 2015 ControlsFX
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.tlcsdm.demo.samples.propertysheet;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address {

    private String addressLine;
    private String suburb;
    private final StringProperty state = new SimpleStringProperty();
    private final StringProperty postcode = new SimpleStringProperty();

    public Address() {
    }

    /**
     * @return the addressLine
     */
    public String getAddressLine() {
        return addressLine;
    }

    /**
     * @param addressLine the addressLine to set
     */
    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    /**
     * @return the suburb
     */
    public String getSuburb() {
        return suburb;
    }

    /**
     * @param suburb the suburb to set
     */
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state.get();
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state.set(state);
    }

    /**
     * @return Property that contains the state.
     */
    public StringProperty stateProperty() {
        return state;
    }

    /**
     * @return the postcode
     */
    public String getPostcode() {
        return postcode.get();
    }

    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(String postcode) {
        this.postcode.set(postcode);
    }

    /**
     * @return Property that contains the postcode.
     */
    public StringProperty postcodeProperty() {
        return postcode;
    }

    @Override
    public String toString() {
        return addressLine + " " + suburb + " " + state.get() + " " + postcode.get();
    }

}
