import org.apache.xmlbeans.XmlException;
import org.ewn.xmlbeans.Dump;
import org.ewn.xmlbeans.LexicalResourceDocument;
import org.ewn.xmlbeans.Query;
import org.ewn.xmlbeans.SenseDocument.Sense;
import org.ewn.xmlbeans.SynsetDocument.Synset;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class QueryTests
{
	private static final String source = System.getProperty("SOURCE");

	private final boolean silent = System.getProperties().containsKey("SILENT");

	private static final String[] WORDS = new String[]{"giggle", "breathe", /* "house", "critical", "galore" */};

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
	}

	@Test
	public void queryWords()
	{
		for (String word : WORDS)
		{
			queryWord(word);
		}
	}

	public void queryWord(String word)
	{
		assertNotNull(document);
		Sense[] senses = Query.querySensesOf(document, word);
		assertNotNull(senses);
		if (!silent)
		{
			System.out.println(word);
		}

		for (Sense sense : senses)
		{
			assertNotNull(sense);
			Dump.dumpSense(sense, "\t", silent);

			Synset synset = Query.querySynsetFromSense(sense);
			assertNotNull(synset);
			Dump.dumpSynset(synset, "\t\t", silent);
			if (!silent)
			{
				System.out.println();
			}
		}
	}
}
