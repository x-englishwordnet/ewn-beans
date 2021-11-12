import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlException;
import org.ewn.xmlbeans.*;
import org.ewn.xmlbeans.DefinitionDocument.Definition;
import org.ewn.xmlbeans.ExampleDocument.Example;
import org.ewn.xmlbeans.LemmaDocument.Lemma;
import org.ewn.xmlbeans.SenseDocument.Sense;
import org.ewn.xmlbeans.SenseRelationDocument.SenseRelation;
import org.ewn.xmlbeans.SynsetDocument.Synset;
import org.ewn.xmlbeans.SynsetRelationDocument.SynsetRelation;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QueryTest
{
	private final String source = System.getProperty("SOURCE");

	private final boolean silent = System.getProperties().containsKey("SILENT");

	private static final String WORD = "giggle";

	private LexicalResourceDocument document;

	@Before
	public void getDocument() throws XmlException, IOException
	{
		if (source == null)
		{
			System.err.printf("Define source file with -DSOURCE=path%n");
			System.exit(1);
		}
		final File xmlFile = new File(source);
		System.out.printf("source=%s%n", xmlFile.getAbsolutePath());
		this.document = LexicalResourceDocument.Factory.parse(xmlFile);
	}

	@Test
	public void queryWord()
	{
		assertNotNull(this.document);
		Sense[] senses = Query.querySensesOf(this.document, WORD);
		assertNotNull(senses);

		for (Sense sense : senses)
		{
			walkSense(sense);

			Synset synset = Query.querySynsetFromSense(sense);
			assertNotNull(synset);
			walkSynset(synset);
			System.out.println();
		}
	}

	public void walkSense(Sense sense)
	{
		String id = sense.getId();
		BigInteger n =  sense.getN();
		System.out.printf("sense id: %s%n", id);
		System.out.printf("\tsensekey: %s%n", Utils.toSensekey(id));
		System.out.printf("\tn: %s%n", n==null? "" : n);

		Lemma lemma = Query.queryLemmaFromSense(sense);
		assertNotNull(lemma);
		System.out.printf("\tlemma: %s%n", lemma.getWrittenForm());

		// adj
		AdjPositionType.Enum adjposition = sense.getAdjposition();
		System.out.printf("\tadjPosition: %s%n", adjposition);

		// verb
		List<String> verbFrames = Query.queryVerbFrames(sense);
		if (verbFrames != null)
		{
			for (String verbFrame : verbFrames)
			{
				assertNotNull(verbFrame);
				System.out.printf("\tverb frame %s%n", verbFrame);
			}
		}
		for (SenseRelation senseRelation : sense.getSenseRelationArray())
		{
			String target = senseRelation.getTarget();
			assertNotNull(target);
			SenseRelationType.Enum type = senseRelation.getRelType();
			assertNotNull(type);
			System.out.printf("\tsense relation %s %s%n", type, target);
		}
	}

	public void walkSynset(Synset synset)
	{
		assertNotNull(this.document);
		assertNotNull(synset);

		System.out.printf("synset id: %s%n", synset.getId());
		Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
		assertTrue(lemmas.length > 0);
		for (Lemma lemma : lemmas)
		{
			System.out.printf("\tmember: %s%n", lemma.getWrittenForm());
		}
		Definition definition = synset.getDefinitionArray(0);
		SimpleValue definitionValue = ((SimpleValue) definition);
		System.out.printf("\tdefinition '%s'%n", definitionValue.getStringValue());
		// Alternatively
		// System.out.printf("\tdefinition %s%n", definition.getStringValue());

		for (Example example : synset.getExampleArray())
		{
			SimpleValue exampleValue = ((SimpleValue) example);
			System.out.printf("\texample %s%n", exampleValue.getStringValue());
			// Alternatively
			// System.out.printf("\texample %s%n", example.getStringValue());
		}

		for (SynsetRelation synsetRelation : synset.getSynsetRelationArray())
		{
			String target = synsetRelation.getTarget();
			assertNotNull(target);
			SynsetRelationType.Enum type = synsetRelation.getRelType();
			assertNotNull(type);
			System.out.printf("\tsense relation %s %s%n", type, target);
		}
	}
}
