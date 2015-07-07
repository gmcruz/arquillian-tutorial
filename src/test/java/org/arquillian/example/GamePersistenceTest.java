/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arquillian.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author cruz
 */
@RunWith(Arquillian.class)
public class GamePersistenceTest {
    
    @Deployment
    public static Archive<?> createDeployment(){
        
        String testName = "GamePersistence_ITTEST.war";
        
        WebArchive container;
        container = ShrinkWrap.create(WebArchive.class, testName)
                .addPackage(Game.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("jbossas-ds.xml");             
        
        System.out.println("");
        System.out.println("*************************************************************************************************************");
        String namePrint = "******************* NEW INTEGRATION TEST SUBMITTED: " + testName + " ";        
        for(int i=0;i<((110-54)-testName.length());i++){namePrint=namePrint+"*";} System.out.println(namePrint);
        System.out.println("*************************************************************************************************************");
        System.out.println(container.toString(true));
        System.out.println("");
        
        return container;        
    }    
    
    private static final String[] GAME_TITLES = {
        "Super Mario Brothers",
        "Mario Kart",
        "F-Zero"        
    };
    
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    UserTransaction utx;
    
    
    @Before
    public void preparePersistenceTest() throws Exception{
        clearData();
        insertData();
        startTransaction();
    }
    
    private void clearData() throws Exception{          
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("DELETE FROM Game").executeUpdate();
        utx.commit();        
    }
    
    private void insertData() throws Exception{        
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for(String title : GAME_TITLES){
            Game game = new Game(title);
            em.persist(game);
        }
        utx.commit();
        //clear the persistenceContext (First Level Cache)
        em.clear();        
    }
    
    private void startTransaction() throws Exception{
        utx.begin();
        em.joinTransaction();
    }
    
    @After
    public void commitTransaction() throws Exception{
        utx.commit();
    }
    
    @Test
    public void shouldFindAllGamesUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllGamesInJpql = "select g from Game g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Game> games = em.createQuery(fetchingAllGamesInJpql, Game.class).getResultList();

        // then
        System.out.println("Found " + games.size() + " games (using JPQL):");
        assertContainsAllGames(games);
    }
    
    private static void assertContainsAllGames(Collection<Game> retrievedGames) {
        Assert.assertEquals(GAME_TITLES.length, retrievedGames.size());
        final Set<String> retrievedGameTitles = new HashSet<String>();
        for (Game game : retrievedGames) {
            System.out.println("* " + game);
            retrievedGameTitles.add(game.getTitle());
        }
        Assert.assertTrue(retrievedGameTitles.containsAll(Arrays.asList(GAME_TITLES)));
    }
    
}
