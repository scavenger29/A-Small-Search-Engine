# A-Small-Search-Engine
Inverted index based search engine

Here we will build the basic data structure underlying search
engines: an inverted index. We will use this inverted index to answer some
simple search queries.

# An inverted index for a set of webpages
Suppose we are given a set of webpages W. For our purposes, each webpage
w belongingt to W will be considered to be a sequence of words w1, w2,...,wk. Another
way of representing the webpage could be to maintain a list of words along
with the position(s) of the words in the webpage. For example consider a
web page with the following text:

Data structures is the study of structures for storing data.

This can be represented as

(data : 1; 10); (structures : 2; 7); (study : 5); (storing : 9)

Note that the small connector words like \is", \the", \of", \for" have not
been stored. Words like this are referred to as stop words and are generally
removed since they are very frequent and normally contain no information
about the content of the webpage.

This representation of the webpage is similar to the index we see at the
back of many books which tell us the page numbers where certain important
terms used in the book may be found. In fact, we can refer to this as an
index for the webpage.

An index is used to find the location of a particular string (word) in a
specific document or webpage, but when we move to a collection of webpages,
we need to first figure out which of the web pages contain the string. For this
we store an inverted index. 

An inverted index for C will contain an entry for each word w belonging to W(C).
This entry will contain tuples of the form (p,k) to indicate that w occurs in
the kth position of page p belonging to C. Using the notation that p[k] denotes the kth
word of page p, we can say that the inverted index of C is defined as

Inv(C) = {(w : {(p, k) : p belongs to C, p[k] = w}) : w belongs to W(C)}

For example, consider the following (small) collection of documents.
1: Data structures is the study of structures for storing data.
2: Structural engineers collect data about structures
The inverted index for this would be
{(data : {(1,1), (1,10), (2,4)}),
(structures: {(1,2), (1,7), (2,6)}),
(study : {(1,5)}),
(storing : {(1,9)}),
(structural : {(2,1)}),
(engineers : {(2,2)}),
(collect : {(2,3)}) }

