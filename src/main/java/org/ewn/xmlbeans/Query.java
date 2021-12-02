package org.ewn.xmlbeans;

import org.apache.xmlbeans.XmlObject;
import org.ewn.xmlbeans.LexicalEntryDocument.LexicalEntry;
import org.ewn.xmlbeans.LemmaDocument.Lemma;
import org.ewn.xmlbeans.SenseDocument.Sense;
import org.ewn.xmlbeans.SynsetDocument.Synset;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Queries
 */
public class Query
{
	// namespaces

	private static final String DECLARE_XQ_NAMESPACE = "declare namespace xq='##local';";

	private static final String DECLARE_DC_NAMESPACE = "declare namespace dc='https://globalwordnet.github.io/schemas/dc/';";

	private static final String DECLARE_NAMESPACES = DECLARE_XQ_NAMESPACE + DECLARE_DC_NAMESPACE;

	// select

	private static final String SELECT_BY_ID = "[@id='%s']";

	// from top

	private static final String QUERY_LEXICALENTRY = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/LexicalEntry"; // "$this//LexicalEntry";

	private static final String QUERY_LEMMA = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/LexicalEntry/Lemma"; // "$this//Lemma";

	private static final String QUERY_SENSE = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/LexicalEntry/Sense"; // "$this//Sense";

	private static final String QUERY_SYNSET = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/Synset"; // "$this//Synset";

	private static final String QUERY_SYNTACTIC_BEHAVIOUR = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/SyntacticBehaviour"; // "$this//SyntacticBehaviour";

	public static LexicalEntry[] queryLexicalEntriesOf(XmlObject top, String word)
	{
		// Query from the top.
		// inefficient: QUERY_LEXICALENTRY + String.format("[count(./Lemma/@writtenForm='%s')>0]", word);
		String query = QUERY_LEMMA + String.format("[@writtenForm='%s']/..", word);
		XmlObject[] result = top.selectPath(query);
		return (LexicalEntry[]) result;
	}

	public static LexicalEntry queryLexicalEntryById(XmlObject top, String id)
	{
		// Query from the top.
		String query = QUERY_LEXICALENTRY + String.format(SELECT_BY_ID, id);
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
		{
			throw new IllegalArgumentException(id);
		}
		if (result.length == 0)
		{
			throw new NoSuchElementException(id);
		}
		return (LexicalEntry) result[0];
	}

	public static Sense querySenseById(XmlObject top, String id)
	{
		// Query from the top.
		String query = QUERY_SENSE + String.format(SELECT_BY_ID, id);
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
		{
			throw new IllegalArgumentException(id);
		}
		if (result.length == 0)
		{
			throw new NoSuchElementException(id);
		}
		return (Sense) result[0];
	}

	public static Sense[] querySensesOf(XmlObject top, String word)
	{
		// Query from the top.
		// alt: QUERY_SENSE + String.format("[../Lemma/@writtenForm='%s']" , word);
		String query = QUERY_LEMMA + String.format("[@writtenForm='%s']/../Sense" , word);
		XmlObject[] result = top.selectPath(query);
		return (Sense[]) result;
	}

	public static Synset querySynsetById(XmlObject top, String id)
	{
		// Query from the top.
		String query = QUERY_SYNSET + String.format(SELECT_BY_ID, id);
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
		{
			throw new IllegalArgumentException(id);
		}
		if (result.length == 0)
		{
			throw new NoSuchElementException(id);
		}
		return (Synset) result[0];
	}

	// from synset

	private static final String QUERY_LEMMAS_FROM_SYNSET = DECLARE_NAMESPACES + "$this/../LexicalEntry/Lemma";

	public static Lemma[] queryLemmasFromSynset(Synset synset)
	{
		String synsetId = synset.getId();
		String query = QUERY_LEMMAS_FROM_SYNSET + "[../Sense/@synset='" + synsetId + "']";
		XmlObject[] result = synset.selectPath(query);
		if (result.length == 0)
		{
			throw new NoSuchElementException(synset.getId());
		}
		return (Lemma[]) result;
	}

	// from sense

	private static final String QUERY_LEX_FROM_SENSE = DECLARE_NAMESPACES + "$this/../../LexicalEntry";

	private static final String QUERY_LEMMA_FROM_SENSE = DECLARE_NAMESPACES + "$this/../Lemma";

	private static final String QUERY_SYNSET_FROM_SENSE = DECLARE_NAMESPACES + "$this/../../Synset";

	private static final String QUERY_SYNTACTIC_BEHAVIOUR_FROM_SENSE = DECLARE_NAMESPACES + "$this/../../SyntacticBehaviour";

	public static Lemma queryLemmaFromSense(Sense sense)
	{
		XmlObject[] result = sense.selectPath(QUERY_LEMMA_FROM_SENSE);
		if (result.length > 1)
		{
			throw new IllegalArgumentException();
		}
		if (result.length == 0)
		{
			throw new NoSuchElementException(sense.getId());
		}
		return (Lemma) result[0];
	}

	public static Synset querySynsetFromSense(Sense sense)
	{
		String synsetId = sense.getSynset();
		String query = QUERY_SYNSET_FROM_SENSE + String.format(SELECT_BY_ID, synsetId);
		XmlObject[] result = sense.selectPath(query);
		if (result.length > 1)
		{
			throw new IllegalArgumentException();
		}
		if (result.length == 0)
		{
			throw new NoSuchElementException(sense.getId());
		}
		return (Synset) result[0];
	}

	public static List<String> queryVerbFrames(Sense sense)
	{
		String senseId = sense.getId();
		String query = QUERY_SYNTACTIC_BEHAVIOUR_FROM_SENSE + "[contains(@senses,'" + senseId + "')]";
		XmlObject[] result = sense.selectPath(query);
		if (result != null)
		{
			List<String> frames = new ArrayList<>();
			for (XmlObject fid : result)
			{
				frames.add(fid.toString());
			}
			return frames;
		}
		return null;
	}
}
