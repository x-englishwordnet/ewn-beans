import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlException;
import org.ewn.xmlbeans.*;
import org.ewn.xmlbeans.DefinitionDocument.Definition;
import org.ewn.xmlbeans.ExampleDocument.Example;
import org.ewn.xmlbeans.LemmaDocument.Lemma;
import org.ewn.xmlbeans.LexicalEntryDocument.LexicalEntry;
import org.ewn.xmlbeans.LexicalResourceDocument.LexicalResource;
import org.ewn.xmlbeans.LexiconDocument.Lexicon;
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

import static org.junit.Assert.*;

public class Tests
{
	private final String source = System.getProperty("SOURCE");

	private final boolean silent = System.getProperties().containsKey("SILENT");

	private static final String WORD = "generate";
	private static final String WORD2 = "suffer";
	private static final String SYNSET_ID = "oewn-00054345-v";
	private static final String SENSE_ID = "oewn-generate__2.29.00..";
	private static final String SENSEKEY = "generate%2:29:00::";

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
	public void getSynsetById()
	{
		assertNotNull(this.document);
		Synset synset = Query.querySynsetById(this.document, SYNSET_ID);
		assertNotNull(synset);
		assertEquals(SYNSET_ID, synset.getId());
		Definition definition = synset.getDefinitionArray(0);
		System.out.printf("%s: %s '%s'%n", "getSynsetById", synset.getId(), ((SimpleValue) definition).getStringValue());
	}

	@Test
	public void getLemmasFromSynset()
	{
		assertNotNull(this.document);
		Synset synset = Query.querySynsetById(this.document, SYNSET_ID);
		assertNotNull(synset);
		assertEquals(SYNSET_ID, synset.getId());
		Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
		assertTrue(lemmas.length > 0);
		for (Lemma lemma : lemmas)
		{
			System.out.printf("%s: %s%n", "getLemmasFromSynset", lemma.getWrittenForm());
		}
	}

	@Test
	public void getSenseById()
	{
		assertNotNull(this.document);
		Sense sense = Query.querySenseById(this.document, SENSE_ID);
		assertNotNull(sense);
		String senseId = sense.getId();
		assertEquals(SENSE_ID, senseId);
		System.out.printf("%s: %s%n", "getSenseById", senseId);
	}

	@Test
	public void getLemmaFromSense()
	{
		assertNotNull(this.document);
		Sense sense = Query.querySenseById(this.document, SENSE_ID);
		assertNotNull(sense);
		assertEquals(SENSE_ID, sense.getId());
		Lemma lemma = Query.queryLemmaFromSense(sense);
		assertNotNull(lemma);
		System.out.printf("%s: %s%n", "getLemmaFromSense", lemma.getWrittenForm());
	}

	@Test
	public void getSynsetFromSense()
	{
		assertNotNull(this.document);
		Sense sense = Query.querySenseById(this.document, SENSE_ID);
		assertNotNull(sense);
		assertEquals(SENSE_ID, sense.getId());
		Synset synset = Query.querySynsetFromSense(sense);
		assertNotNull(synset);
		Definition definition = synset.getDefinitionArray(0);
		System.out.printf("%s: %s '%s'%n", "getSynsetFromSense", synset.getId(), ((SimpleValue) definition).getStringValue());
	}

	@Test
	public void getSenseBySensekey()
	{
		String senseId = Utils.toId(SENSEKEY);
		assertNotNull(this.document);
		Sense sense = Query.querySenseById(this.document, senseId);
		assertNotNull(sense);
		String senseId2 = Utils.toSensekey(sense.getId());
		assertEquals(SENSEKEY, senseId2);
		System.out.printf("%s: %s%n", "getSenseBySensekey", senseId2);}

	@Test
	public void getSensesByWord()
	{
		assertNotNull(this.document);
		Sense[] senses = Query.querySensesOf(this.document, WORD2);
		assertNotNull(senses);
		for (Sense sense : senses)
		{
			System.out.printf("%s: %s%n", "getSensesOf", sense.getIdentifier());
		}
	}

	@Test
	public void scanSenses()
	{
		assertNotNull(this.document);
		final LexicalResource lexicalResource = document.getLexicalResource();
		assertNotNull(lexicalResource);
		final Lexicon lexicon = lexicalResource.getLexiconArray()[0];
		assertNotNull(lexicon);

		for (LexicalEntry entry : lexicon.getLexicalEntryArray())
		{
			Lemma lemma = entry.getLemma();
			assertNotNull(lemma);
			PartOfSpeechType.Enum pos = lemma.getPartOfSpeech();
			assertNotNull(pos);
			String writtenForm = lemma.getWrittenForm();
			assertNotNull(writtenForm);
			WrittenFormType writtenFormType = lemma.xgetWrittenForm();
			assertNotNull(writtenFormType);

			for (Sense sense : entry.getSenseArray())
			{
				String id = sense.getId();
				assertNotNull(id);
				String synsetId = sense.getSynset();
				assertNotNull(synsetId);

				BigInteger n = sense.getN();
				//assertNotNull(n);
				String identifier = sense.getIdentifier();
				//assertNotNull(identifier);

				// adj
				AdjPositionType.Enum adjPosition = sense.getAdjposition();

				// verb
				List<String> verbFrames = Query.queryVerbFrames(sense);
				if (verbFrames != null)
				{
					for (String verbFrame : verbFrames)
					{
						assertNotNull(verbFrame);
						//System.out.printf("verb frame %s%n", verbFrame);
					}
				}
				for (SenseRelation senseRelation : sense.getSenseRelationArray())
				{
					String target = senseRelation.getTarget();
					assertNotNull(target);
					SenseRelationType type = senseRelation.xgetRelType();
					assertNotNull(type);
					SenseIDREFType idref = senseRelation.xgetTarget();
					assertNotNull(idref);
				}
			}
		}
	}

	@Test
	public void scanSynsets()
	{
		assertNotNull(this.document);
		final LexicalResource lexicalResource = this.document.getLexicalResource();
		assertNotNull(lexicalResource);
		final Lexicon lexicon = lexicalResource.getLexiconArray()[0];
		assertNotNull(lexicon);

		for (Synset synset : lexicon.getSynsetArray())
		{
			LexFileType.Enum subject = synset.getSubject();

			PartOfSpeechType.Enum pos = synset.getPartOfSpeech();
			assertNotNull(pos);
			DefinitionDocument.Definition definition = synset.getDefinitionArray(0);
			assertNotNull(definition);

			for (Example example : synset.getExampleArray())
			{
				assertNotNull(example);
			}
			for (SynsetRelation synsetRelation : synset.getSynsetRelationArray())
			{
				String target = synsetRelation.getTarget();
				assertNotNull(target);
				SynsetRelationType type = synsetRelation.xgetRelType();
				assertNotNull(type);
				SynsetIDREFType idref = synsetRelation.xgetTarget();
				assertNotNull(idref);
			}
		}
	}
}
