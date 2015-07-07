package org.arquillian.example;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GreeterTest {

    @Deployment
    public static JavaArchive createDeployment() {  
        
        String testName = "GreeterTest_ITTEST.jar";
        JavaArchive container;
        container = ShrinkWrap.create(JavaArchive.class, testName)
                .addClass(Greeter.class)
                .addClass(PhraseBuilder.class)
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
    Greeter greeter;

    @Test
    public void should_create_greeting() {        
        String expected = greeter.createGreeting("Guillermo");
        Assert.assertEquals("Hello, Guillermo!", expected);
        greeter.greet(System.out, "Guillermo");
    }
}