import org.apache.xmlbeans.XmlException;
import org.ewn.xmlbeans.LemmaDocument.Lemma;
import org.ewn.xmlbeans.LexicalEntryDocument.LexicalEntry;
import org.ewn.xmlbeans.LexicalResourceDocument;
import org.ewn.xmlbeans.Query;
import org.ewn.xmlbeans.SenseDocument.Sense;
import org.ewn.xmlbeans.Strings;
import org.ewn.xmlbeans.SynsetDocument.Synset;
import org.ewn.xmlbeans.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class Tests
{
	private static final String source = System.getProperty("SOURCE");

	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	private static final String WORD = "generate";
	private static final String WORD2 = "suffer";
	private static final String WORD3 = "smile";

	private static final String LEX_ID1 = "oewn-generate-v";
	private static final String LEX_ID2 = "oewn-suffer-v";
	private static final String LEX_ID3 = "oewn-smile-v";

	private static final String SENSE_ID1 = "oewn-generate__2.29.00..";
	private static final String SENSE_ID2 = "oewn-suffer__2.29.01..";
	private static final String SENSE_ID3 = "oewn-smile__2.32.00..";

	private static final String SENSEKEY1 = "generate%2:29:00::";
	private static final String SENSEKEY2 = "suffer%2:29:01::";
	private static final String SENSEKEY3 = "smile%2:32:00::";

	private static final String SYNSET_ID1 = "oewn-00054345-v";
	private static final String SYNSET_ID2 = "oewn-00065410-v";
	private static final String SYNSET_ID3 = "oewn-01069534-v";

	private static LexicalResourceDocument document;

	@BeforeClass
	public static void getDocument() throws XmlException, IOException
	{
		if (source == null)
		{
			System.err.printf("Define source file with -DSOURCE=path%n");
			System.exit(1);
		}
		final File xmlFile = new File(source);
		System.out.printf("source=%s%n", xmlFile.getAbsolutePath());
		if (!xmlFile.exists())
		{
			System.err.println("Define XML source dir that exists");
			System.exit(2);
		}
		document = LexicalResourceDocument.Factory.parse(xmlFile);
		assertNotNull(document);
	}

	@Test
	public void getLexById()
	{
		for (String lexId : Arrays.asList(LEX_ID1, LEX_ID2, LEX_ID3))
		{
			ps.printf("%s(%s):%n", "getLexById", lexId);

			LexicalEntry lex = Query.queryLexicalEntryById(document, lexId);
			assertNotNull(lex);
			assertEquals(lexId, lex.getId());
			ps.printf("\t%s%n", lex.getId());
		}
	}

	@Test
	public void getSynsetById()
	{
		for (String synsetId : Arrays.asList(SYNSET_ID1, SYNSET_ID2, SYNSET_ID3))
		{
			ps.printf("%s(%s):%n", "getSynsetById", synsetId);

			Synset synset = Query.querySynsetById(document, synsetId);
			assertNotNull(synset);
			assertEquals(synsetId, synset.getId());
			ps.printf("\t%s%n", Strings.toString(synset));
		}
	}

	@Test
	public void getLemmasFromSynset()
	{
		for (String synsetId : Arrays.asList(SYNSET_ID1, SYNSET_ID2, SYNSET_ID3))
		{
			ps.printf("%s(%s):%n", "getLemmasFromSynset", synsetId);

			Synset synset = Query.querySynsetById(document, synsetId);
			assertNotNull(synset);
			assertEquals(synsetId, synset.getId());
			Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
			assertTrue(lemmas.length > 0);
			for (Lemma lemma : lemmas)
			{
				ps.printf("\t%s%n", lemma.getWrittenForm());
			}
		}
	}

	@Test
	public void getSenseById()
	{
		for (String senseId1 : Arrays.asList(SENSE_ID1, SENSE_ID2, SENSE_ID3))
		{
			ps.printf("%s(%s):%n", "getSenseById", senseId1);

			Sense sense = Query.querySenseById(document, senseId1);
			assertNotNull(sense);
			String senseId2 = sense.getId();
			assertEquals(senseId1, senseId2);
			ps.printf("\t%s%n", senseId2);
		}
	}

	@Test
	public void getLemmaFromSense()
	{
		for (String senseId : Arrays.asList(SENSE_ID1, SENSE_ID2, SENSE_ID3))
		{
			ps.printf("%s(%s):%n", "getLemmaFromSense", senseId);

			Sense sense = Query.querySenseById(document, senseId);
			assertNotNull(sense);
			assertEquals(senseId, sense.getId());
			Lemma lemma = Query.queryLemmaFromSense(sense);
			assertNotNull(lemma);
			ps.printf("\t%s%n", lemma.getWrittenForm());

		}
	}

	@Test
	public void getSynsetFromSense()
	{
		for (String senseId : Arrays.asList(SENSE_ID1, SENSE_ID2, SENSE_ID3))
		{
			ps.printf("%s(%s):%n", "getSynsetFromSense", senseId);

			Sense sense = Query.querySenseById(document, senseId);
			assertNotNull(sense);
			assertEquals(senseId, sense.getId());
			Synset synset = Query.querySynsetFromSense(sense);
			assertNotNull(synset);
			ps.printf("\t%s%n", Strings.toString(synset));
		}
	}

	@Test
	public void getSenseBySensekey()
	{
		for (String sensekey : Arrays.asList(SENSEKEY1, SENSEKEY2, SENSEKEY3))
		{
			ps.printf("%s(%s):%n", "getSenseBySensekey", sensekey);

			String senseId1 = Utils.toId(sensekey);
			Sense sense = Query.querySenseById(document, senseId1);
			assertNotNull(sense);
			String senseId2 = Utils.toSensekey(sense.getId());
			assertEquals(sensekey, senseId2);
			ps.printf("\t%s %s%n", senseId1, senseId2);
		}
	}

	@Test
	public void getSensesByWord()
	{
		for (String word : Arrays.asList(WORD, WORD2, WORD3))
		{
			ps.printf("%s(%s):%n", "getSensesByWord", word);

			Sense[] senses = Query.querySensesOf(document, word);
			assertNotNull(senses);
			for (Sense sense : senses)
			{
				ps.printf("\t%s%n", Utils.toSensekey(sense.getId()));
			}
		}
	}
}
