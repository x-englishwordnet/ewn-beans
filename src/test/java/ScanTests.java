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
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ScanTests
{
	private static final String source = System.getProperty("SOURCE2");

	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

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
	public void scanLexes()
	{
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
			ps.printf("%s %s%n", writtenForm, Strings.toPronunciationsString(lemma));
		}
	}

	@Test
	public void scanSenses()
	{
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
				ps.printf("%s%n", Strings.toPair(sense));

				String id = sense.getId();
				assertNotNull(id);
				String synsetId = sense.getSynset();
				assertNotNull(synsetId);

				BigInteger n = sense.getN();
				//assertNotNull(n); // not true in merged OEWN
				String identifier = sense.getIdentifier();
				//assertNotNull(identifier); // not true in OEWN 2020, id replaces it

				// adj
				AdjPositionType.Enum adjPosition = sense.getAdjposition();
				//assertNotNull(identifier); // not true for all words

				// verb
				List<String> verbFrames = Query.queryVerbFrames(sense);
				if (verbFrames != null)
				{
					for (String verbFrame : verbFrames)
					{
						assertNotNull(verbFrame);
						//ps.printf("verb frame %s%n", verbFrame);
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
		final LexicalResource lexicalResource = document.getLexicalResource();
		assertNotNull(lexicalResource);
		final Lexicon lexicon = lexicalResource.getLexiconArray()[0];
		assertNotNull(lexicon);

		for (Synset synset : lexicon.getSynsetArray())
		{
			ps.printf("%s%n", Strings.toString(synset));

			LexFileType.Enum lexFile = synset.getLexfile();
			// assertNotNull(lexFile); // not true in non merged files

			PartOfSpeechType.Enum pos = synset.getPartOfSpeech();
			assertNotNull(pos);
			Definition definition = synset.getDefinitionArray(0);
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
