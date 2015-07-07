package org.arquillian.example;

import java.io.PrintStream;
import javax.inject.Inject;

/**
 * A component for creating personal greetings.
 */
public class Greeter {
    
    
    private PhraseBuilder phraseBuilder;
    
    @Inject
    public Greeter(PhraseBuilder phrasebuilder){        
        this.phraseBuilder = phrasebuilder;        
    }
    
    public void greet(PrintStream to, String name) {
        to.println(createGreeting(name));
    }

    public String createGreeting(String name) {
        return phraseBuilder.buildPhrase("hello", name);
    }
}
