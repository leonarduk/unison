/// **
// * NNTPNewsGroup
// *
// * @author ${author}
// * @since 17-Jun-2016
// */
// package uk.co.sleonard.unison.input;
//
// import org.apache.commons.net.nntp.NewsgroupInfo;
//
/// **
// * The Class NNTPNewsGroup.
// *
// * @author Stephen <github@leonarduk.com>
// * @since v1.0.0
// *
// */
// public class NNTPNewsGroup implements Comparable<NNTPNewsGroup> {
// /** The __estimated article count. */
// private int __estimatedArticleCount;
//
// /** The __last article. */
// private int __firstArticle;
//
// private int __lastArticle;
//
// /** The __newsgroup. */
// private String __newsgroup;
//
// public NNTPNewsGroup(final int estimatedArticleCount, final int firstArticle,
// final int lastArticle, final String newsgroup) {
// super();
// this.__estimatedArticleCount = estimatedArticleCount;
// this.__firstArticle = firstArticle;
// this.__lastArticle = lastArticle;
// this.__newsgroup = newsgroup;
// }
//
// /**
// * Instantiates a new NNTP news group.
// *
// * @param original
// * the original
// */
// @SuppressWarnings("deprecation")
// public NNTPNewsGroup(final NewsgroupInfo original) {
// this.__estimatedArticleCount = original.getArticleCount();
// this.__firstArticle = original.getFirstArticle();
// this.__lastArticle = original.getLastArticle();
// this.__newsgroup = original.getNewsgroup();
// }
//
// /*
// * (non-Javadoc)
// *
// * @see java.lang.Comparable#compareTo(java.lang.Object)
// */
// @Override
// public int compareTo(final NNTPNewsGroup that) {
// return this.getNewsgroup().compareTo(that.getNewsgroup());
// }
//
// /***************************************************************************
// * Get the estimated number of articles in the newsgroup. The accuracy of this value will depend
// * on the server implementation.
// * <p>
// *
// * @return The estimated number of articles in the newsgroup.
// **************************************************************************/
// public int getArticleCount() {
// final int total = this.__lastArticle - this.__firstArticle;
// if (total < this.__estimatedArticleCount) {
// return total;
// }
// return this.__estimatedArticleCount;
// }
//
// /***************************************************************************
// * Get the number of the first article in the newsgroup.
// * <p>
// *
// * @return The number of the first article in the newsgroup.
// **************************************************************************/
// public int getFirstArticle() {
// return this.__firstArticle;
// }
//
// /***************************************************************************
// * Get the number of the last article in the newsgroup.
// * <p>
// *
// * @return The number of the last article in the newsgroup.
// **************************************************************************/
// public int getLastArticle() {
// return this.__lastArticle;
// }
//
// /***************************************************************************
// * Get the newsgroup name.
// * <p>
// *
// * @return The name of the newsgroup.
// **************************************************************************/
// public String getNewsgroup() {
// return this.__newsgroup;
// }
//
// /**
// * _set article count.
// *
// * @param count
// * the count
// */
// void setArticleCount(final int count) {
// this.__estimatedArticleCount = count;
// }
//
// /**
// * _set first article.
// *
// * @param first
// * the first
// */
// void setFirstArticle(final int first) {
// this.__firstArticle = first;
// }
//
// /**
// * _set last article.
// *
// * @param last
// * the last
// */
// void setLastArticle(final int last) {
// this.__lastArticle = last;
// }
//
// /**
// * _set newsgroup.
// *
// * @param newsgroup
// * the newsgroup
// */
// void setNewsgroup(final String newsgroup) {
// this.__newsgroup = newsgroup;
// }
//
// /*
// * (non-Javadoc)
// *
// * @see java.lang.Object#toString()
// */
// @Override
// public String toString() {
// return this.getNewsgroup() + " (" + this.getArticleCount() + ")";
// }
//
// /*
// * public String toString() { StringBuffer buffer = new StringBuffer();
// * buffer.append(__newsgroup); buffer.append(' '); buffer.append(__lastArticle); buffer.append('
// * '); buffer.append(__firstArticle); buffer.append(' '); switch(__postingPermission) { case 1:
// * buffer.append('m'); break; case 2: buffer.append('y'); break; case 3: buffer.append('n');
// * break; } return buffer.toString(); }
// */
// }
