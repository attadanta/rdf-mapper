package eu.dareed.rdfmapper;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class VariableResolutionTest {
    @Test
    public void testNamedVariableResolutionInTheInterior() {
        String userString = ":x a <${ssn.Observation}>";
        String expectedURL = "http://purl.oclc.org/NET/ssnx/ssn#Observation";

        VariableResolver resolver = mock(VariableResolver.class);
        when(resolver.resolveNamedVariable("ssn.Observation")).thenReturn(expectedURL);

        Context context = new Context(resolver);
        Assert.assertEquals(String.format(":x a <%s>", expectedURL), context.resolveVariables(userString));
    }

    @Test
    public void testNamedVariableResolutionAtTheLeftBorder() {
        String userString = "${ssn.Observation} a owl:Class";
        String expectedURL = "ssn:Observation";

        VariableResolver resolver = mock(VariableResolver.class);
        when(resolver.resolveNamedVariable("ssn.Observation")).thenReturn(expectedURL);

        Context context = new Context(resolver);
        Assert.assertEquals(String.format("%s a owl:Class", expectedURL), context.resolveVariables(userString));
    }

    @Test
    public void testNamedVariableResolutionAtTheRightBorder() {
        String userString = ":x a ${ssn.Observation}";
        String expectedURL = "ssn:Observation";

        VariableResolver resolver = mock(VariableResolver.class);
        when(resolver.resolveNamedVariable("ssn.Observation")).thenReturn(expectedURL);

        Context context = new Context(resolver);
        Assert.assertEquals(String.format(":x a %s", expectedURL), context.resolveVariables(userString));
    }

    @Test
    public void testIndexedVariableResolutionInTheInterior() {
        String userString = "#{69}:Observation a owl:Class";
        String expectedURL = "ssn:Observation";

        VariableResolver resolver = mock(VariableResolver.class);
        when(resolver.resolveIndex(69)).thenReturn("ssn");

        Context context = new Context(resolver);
        Assert.assertEquals(String.format("%s a owl:Class", expectedURL), context.resolveVariables(userString));
    }

    @Test
    public void testIndexedVariableResolutionAtTheLeftBorder() {
        String userString = ":x a <http://energyplus.net/resoruces/#{42}>";
        String expectedURL = "http://energyplus.net/resoruces/Plant";

        VariableResolver resolver = mock(VariableResolver.class);
        when(resolver.resolveIndex(42)).thenReturn("Plant");

        Context context = new Context(resolver);
        Assert.assertEquals(String.format(":x a <%s>", expectedURL), context.resolveVariables(userString));
    }

    @Test
    public void testIndexedVariableResolutionAtTheRightBorder() {
        String userString = ":x a eplus:#{42}";
        String expectedURL = "eplus:Plant";

        VariableResolver resolver = mock(VariableResolver.class);
        when(resolver.resolveIndex(42)).thenReturn("Plant");

        Context context = new Context(resolver);
        Assert.assertEquals(String.format(":x a %s", expectedURL), context.resolveVariables(userString));
    }

    @Test
    public void testVariableResolutionCombo() {
        String userString = "<${our.house}> a eplus:#{42}";

        VariableResolver resolver = mock(VariableResolver.class);
        when(resolver.resolveNamedVariable("our.house")).thenReturn("http://example.com/House");
        when(resolver.resolveIndex(42)).thenReturn("Building");

        Context context = new Context(resolver);
        Assert.assertEquals("<http://example.com/House> a eplus:Building", context.resolveVariables(userString));
    }

    @Test
    public void testInvertedVariableResolutionCombo() {
        String userString = "example:#{42} a ${ssn}FeatureOfInterest";

        VariableResolver resolver = mock(VariableResolver.class);
        when(resolver.resolveIndex(42)).thenReturn("House42");
        when(resolver.resolveNamedVariable("ssn")).thenReturn("http://purl.oclc.org/NET/ssnx/ssn#");

        Context context = new Context(resolver);
        Assert.assertEquals("example:House42 a http://purl.oclc.org/NET/ssnx/ssn#FeatureOfInterest", context.resolveVariables(userString));
    }
}
