package org.ewn.xmlbeans;

import org.apache.xmlbeans.XmlObject;
import org.ewn.xmlbeans.LemmaDocument.Lemma;
import org.ewn.xmlbeans.SenseDocument.Sense;
import org.ewn.xmlbeans.SynsetDocument.Synset;

import java.util.ArrayList;
import java.util.List;

public class Query
{
	private static final String DECLARE_XQ_NAMESPACE = "declare namespace xq='##local';";

	private static final String DECLARE_DC_NAMESPACE = "declare namespace dc='https://globalwordnet.github.io/schemas/dc/';";

	private static final String DECLARE_NAMESPACES = DECLARE_XQ_NAMESPACE + DECLARE_DC_NAMESPACE;


	// private static String QUERY_SYNSET = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/Synset";
	private static final String QUERY_SYNSET = DECLARE_NAMESPACES + "$this//Synset";

	//private static String QUERY_SENSE = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/LexicalEntry/Sense";
	private static final String QUERY_SENSE = DECLARE_NAMESPACES + "$this//Sense";

	//private static String QUERY_SYNTACTIC_BEHAVIOUR = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/SyntacticBehaviour";
	private static final String QUERY_SYNTACTIC_BEHAVIOUR = DECLARE_NAMESPACES + "$this//SyntacticBehaviour";

	private static final String QUERY_LEMMA_FROM_SENSE = DECLARE_NAMESPACES + "$this/../Lemma";

	private static final String QUERY_LEMMAS_FROM_SYNSET = DECLARE_NAMESPACES + "$this/../LexicalEntry/Lemma";

	private static final String QUERY_SYNSET_FROM_SENSE = DECLARE_NAMESPACES + "$this/../../Synset";

	private static final String QUERY_SYNTACTIC_BEHAVIOUR_FROM_SENSE = DECLARE_NAMESPACES + "$this/../SyntacticBehaviour";

	public static Synset querySynsetById(XmlObject top, String id)
	{
		// Query from the top.
		String query = QUERY_SYNSET + "[@id='" + id + "']";
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
		{
			throw new IllegalArgumentException();
		}
		if (result.length == 0)
		{
			throw new IllegalArgumentException();
		}
		return (Synset) result[0];
	}

	public static Sense querySenseById(XmlObject top, String id)
	{
		String query = QUERY_SENSE + "[@id='" + id + "']";
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
		{
			throw new IllegalArgumentException();
		}
		if (result.length == 0)
		{
			throw new IllegalArgumentException();
		}
		return (Sense) result[0];
	}

	public static Lemma queryLemmaFromSense(Sense sense)
	{
		XmlObject[] result = sense.selectPath(QUERY_LEMMA_FROM_SENSE);
		if (result.length > 1)
		{
			throw new IllegalArgumentException();
		}
		if (result.length == 0)
		{
			throw new IllegalArgumentException();
		}
		return (Lemma) result[0];
	}

	public static Lemma[] queryLemmasFromSynset(Synset synset)
	{
		String synsetId = synset.getId();
		String query = QUERY_LEMMAS_FROM_SYNSET + "[../Sense/@synset='" + synsetId + "']";
		XmlObject[] result = synset.selectPath(query);
		if (result.length == 0)
		{
			throw new IllegalArgumentException();
		}
		return (Lemma[]) result;
	}

	public static Synset querySynsetFromSense(Sense sense)
	{
		String synsetId = sense.getSynset();
		String query = QUERY_SYNSET_FROM_SENSE + "[@id='" + synsetId + "']";
		XmlObject[] result = sense.selectPath(query);
		if (result.length > 1)
		{
			throw new IllegalArgumentException();
		}
		if (result.length == 0)
		{
			throw new IllegalArgumentException();
		}
		return (Synset) result[0];
	}

	public static Sense[] querySensesOf(XmlObject top, String word)
	{
		String query = QUERY_SENSE + "[../Lemma/@writtenForm='" + word + "']";

		// Query from the top.
		XmlObject[] result = top.selectPath(query);
		return (Sense[]) result;
	}

	public static Synset[] querySynsets(XmlObject top, String query)
	{
		// Query from the top.
		XmlObject[] result = top.selectPath(query);
		return (Synset[]) result;
	}

	public static Sense[] querySenses(XmlObject top, String query)
	{
		// Query from the top.
		XmlObject[] result = top.selectPath(query);
		return (Sense[]) result;
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
