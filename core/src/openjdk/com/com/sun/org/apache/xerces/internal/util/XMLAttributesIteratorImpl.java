/*
 * Copyright 2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.org.apache.xerces.internal.util;

//java imports
import java.util.Iterator ;
import java.util.NoSuchElementException;

//xerces imports
import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl ;

/**
 *
 * @author  Neeraj Bajaj, Sun Microsystems
 */

/**
 * Its better to extend the functionality of existing XMLAttributesImpl and also make it of type Iterator.
 * We can directly  give an object of type iterator from StartElement event. We should also have
 * Attribute object of type javax.xml.stream.Attribute internally. It would avoid the need of creating
 * new javax.xml.stream.Attribute object at the later stage.
 *
 * Should we change XMLAttributes interface to implement Iteraotr ? I think its better avoid touching XNI as
 * much as possible. - NB.
 */

public class XMLAttributesIteratorImpl extends XMLAttributesImpl implements Iterator {
    
    //pointer to current position.
    protected int fCurrent = 0 ;
    
    protected XMLAttributesImpl.Attribute fLastReturnedItem ;
    
    /** Creates a new instance of XMLAttributesIteratorImpl */
    public XMLAttributesIteratorImpl() {
    }
    
    public boolean hasNext() {
        return fCurrent < getLength() ? true : false ;
    }//hasNext()
    
    public Object next() {
        if(hasNext()){
            // should this be of type javax.xml.stream.Attribute ?
            return fLastReturnedItem = fAttributes[fCurrent++] ;
        }
        else{
            throw new NoSuchElementException() ;
        }
    }//next
    
    public void remove() {
        //make sure that only last returned item can be removed.
        if(fLastReturnedItem == fAttributes[fCurrent - 1]){
            //remove the attribute at current index and lower the current position by 1.
            removeAttributeAt(fCurrent--) ;
        }
        else {
            //either the next method has been called yet, or the remove method has already been called
            //after the last call to the next method.
            throw new IllegalStateException();
        }
    }//remove
    
    public void removeAllAttributes() {
        super.removeAllAttributes() ;
        fCurrent = 0 ;
    }
    /** xxx: should we be doing this way ? Attribute event defines so many functions which doesn't make any sense
     *for Attribute.
     *
     */
    /*
    class AttributeImpl extends com.sun.org.apache.xerces.internal.util.XMLAttributesImpl.Attribute implements javax.xml.stream.events.Attribute{
     
    }
     */
    
} //XMLAttributesIteratorImpl