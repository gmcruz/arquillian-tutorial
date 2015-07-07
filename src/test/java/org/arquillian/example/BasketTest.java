/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arquillian.example;

import javax.ejb.EJB;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;



/**
 *
 * @author cruz
 */

@RunWith(Arquillian.class)
public class BasketTest {
    
    @Deployment
    public static JavaArchive creteDeployment(){
        
        String testName = "BasketProcess_ITTEST.jar";        
        JavaArchive container;
        
        container = ShrinkWrap.create(JavaArchive.class, testName)
                .addClasses(Basket.class, OrderRepository.class, SingletonOrderRepository.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        System.out.println("");
        System.out.println("*************************************************************************************************************");
        String namePrint = "******************* NEW INTEGRATION TEST SUBMITTED: " + testName + " ";        
        for(int i=0;i<((110-54)-testName.length());i++){namePrint=namePrint+"*";} System.out.println(namePrint);
        System.out.println("*************************************************************************************************************");
        System.out.println(container.toString(true));
        System.out.println("");
        
        return container;
    }

   @Inject
   Basket basket;
   
   @EJB
   OrderRepository repo;
   
    @Test
    @InSequence(1)
    public void place_order_should_add_order() {
        basket.addItem("sunglasses");
        basket.addItem("suit");
        basket.placeOrder();
        Assert.assertEquals(1, repo.getOrderCount());
        Assert.assertEquals(0, basket.getItemCount());

        basket.addItem("raygun");
        basket.addItem("spaceship");
        basket.placeOrder();
        Assert.assertEquals(2, repo.getOrderCount());
        Assert.assertEquals(0, basket.getItemCount());
    }

    @Test
    @InSequence(2)
    public void order_should_be_persistent() {
        Assert.assertEquals(2, repo.getOrderCount());
    }
    
}
