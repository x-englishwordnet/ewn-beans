import org.apache.xmlbeans.XmlException;
import org.ewn.xmlbeans.Dump;
import org.ewn.xmlbeans.LexicalEntryDocument.LexicalEntry;
import org.ewn.xmlbeans.LexicalResourceDocument;
import org.ewn.xmlbeans.Query;
import org.ewn.xmlbeans.SenseDocument.Sense;
import org.ewn.xmlbeans.SynsetDocument.Synset;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertNotNull;

public class QueryTests
{
	private static final String source = System.getProperty("SOURCE");

	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	private static final String[] WORDS = new String[]{"smile", "giggle", "breathe", /* "house", "critical", "galore" */};

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
		System.out.printf("loaded%n");
	}

	@Test
	public void queryLexesOfWords()
	{
		for (String word : WORDS)
		{
			queryLexesOfWords(word);
		}
	}

	@Test
	public void querySensesOfWords()
	{
		for (String word : WORDS)
		{
			querySensesOfWord(word);
		}
	}

	public void queryLexesOfWords(String word)
	{
		ps.printf("query lexes of '%s'%n", word);

		LexicalEntry[] lexes = Query.queryLexicalEntriesOf(document, word);
		assertNotNull(lexes);
		for (LexicalEntry lex : lexes)
		{
			assertNotNull(lex);
			ps.printf("lex: %s%n", lex);
			Dump.dumpLex(lex, "\t", ps);
			ps.println();
		}
	}

	public void querySensesOfWord(String word)
	{
		ps.printf("query senses of '%s'%n", word);

		Sense[] senses = Query.querySensesOf(document, word);
		assertNotNull(senses);
		for (Sense sense : senses)
		{
			assertNotNull(sense);
			Dump.dumpSense(sense, "\t", ps);

			Synset synset = Query.querySynsetFromSense(sense);
			assertNotNull(synset);
			Dump.dumpSynset(synset, "\t\t", ps);
			ps.println();
		}
	}
}
