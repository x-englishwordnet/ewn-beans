package org.ewn.xmlbeans;

import java.util.function.Function;

/**
 * Utilities
 */
public class Utils
{
	private Utils()
	{
	}

	/**
	 * Convert ID to sensekey
	 *
	 * @param senseId sense id
	 * @return sensekey
	 */
	static public String toSensekey(String senseId)
	{
		String sk = senseId.substring("oewn-".length());
		int b = sk.indexOf("__");

		String lemma = sk.substring(0, b) //
				.replace("-ap-", "'") //
				.replace("-lb-", "(") // extension
				.replace("-rb-", ")") // extension
				.replace("-sl-", "/") //
				.replace("-ex-", "!") //
				.replace("-cm-", ",") //
				.replace("-cl-", ":") //
				.replace("-sp-", "_"); // extension

		String tail = sk.substring(b + 2) //
				.replace(".", ":") //
				.replace("-ap-", "'") // extension
				.replace("-lb-", "(") // extension
				.replace("-rb-", ")") // extension
				.replace("-sl-", "/") // extension
				.replace("-ex-", "!") // extension
				.replace("-cm-", ",") // extension
				.replace("-cl-", ":") // extension
				.replace("-sp-", "_");

		return lemma + '%' + tail;
	}

	/*
	def unmap_sense_key(sk):
	    if "__" in sk:
	        e = sk.split("__")
	        l = e[0][KEY_PREFIX_LEN:]
	        r = "__".join(e[1:])
	        return (
	        l
		        .replace("-ap-", "'")
		        .replace("-sl-", "/")
		        .replace("-ex-", "!")
		        .replace("-cm-",",")
		        .replace("-cl-",":")
	        + "%" +
	        r
		        .replace(".", ":")
		        .replace("-sp-","_"))
	    else:
	        return sk[KEY_PREFIX_LEN:]
		        .replace("__", "%")
		        .replace("-ap-", "'")
		        .replace("-sl-", "/")
		        .replace("-ex-", "!")
		        .replace("-cm-",",")
		        .replace("-cl-",":")
	*/

	/**
	 * Convert sensekey to ID
	 *
	 * @param sensekey sensekey
	 * @return sensekey
	 */
	static public String toId(String sensekey)
	{
		int b = sensekey.indexOf('%');

		String lemma = sensekey.substring(0, b) //
				.replace("'", "-ap-") //
				.replace("(", "-lb-") // extension
				.replace(")", "-rb-") // extension
				.replace("/", "-sl-") //
				.replace("!", "-ex-") //
				.replace(",", "-cm-") //
				.replace(":", "-cl-") //
				.replace("_", "-sp-"); // extension

		String tail = sensekey.substring(b + 1) //
				.replace("_", "-sp-") //
				.replace("'", "-ap-") // extension
				.replace("(", "-lb-") // extension
				.replace(")", "-rb-") // extension
				.replace("/", "-sl-") // extension
				.replace(",", "-cm-") // extension
				.replace("!", "-ex-") // extension
				.replace(":", ".");

		return "oewn-" + lemma + "__" + tail;
	}
	/*
	def map_sense_key(sk):
	    if "%" in sk:
	        e = sk.split("%")
	        return ("oewn-" +
		        e[0]
			        .replace("'","-ap-")
			        .replace("/","-sl-")
			        .replace("!","-ex-")
			        .replace(",","-cm-")
			        .replace(":","-cl-")
			    + "__" +
			    e[1]
			        .replace("_","-sp-")
			        .replace(":","."))
	    else:
	        return "oewn-" + sk
		        .replace("%", "__")
		        .replace("'","-ap-")
		        .replace("/","-sl-")
		        .replace("!","-ex-")
		        .replace(",","-cm-")
		        .replace(":","-cl-")
	*/

	/**
	 * Join items
	 *
	 * @param <T>   type of items
	 * @param items array of items of type T
	 * @param delim delimiter
	 * @param f     string function to represent item
	 * @return joined string representation of items
	 */
	public static <T> String join(T[] items, char delim, Function<T, String> f)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T item : items)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delim);
			}
			String value = f.apply(item);
			sb.append(value);
		}
		return sb.toString();
	}

	/**
	 * Join items
	 *
	 * @param <T>   type of items
	 * @param items iteration of items of type T
	 * @param delim delimiter
	 * @param f     string function to represent item
	 * @return joined string representation of items
	 */
	public static <T> String join(Iterable<T> items, char delim, Function<T, String> f)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T item : items)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delim);
			}
			String value = f.apply(item);
			sb.append(value);
		}
		return sb.toString();
	}
}
