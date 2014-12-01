/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/

/*
 * Part or all of this source file was forked from a third-party project, the details of which are listed below.
 *
 * Source Project: Totorom
 * Source URL: https://github.com/BrynCooke/totorom
 * Source License: Apache Public License v2.0
 * When: November, 20th 2014
 */
package com.syncleus.ferma;

import java.util.*;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.gremlin.Tokens;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.transform.TransformPipe;
import com.tinkerpop.pipes.util.structures.Pair;
import com.tinkerpop.pipes.util.structures.Row;
import com.tinkerpop.pipes.util.structures.Table;
import com.tinkerpop.pipes.util.structures.Tree;

/**
 * The root traversal class. Wraps a Tinkerpop {@link GremlinPipeline}
 *
 * @param <T> The type of the objects coming off the pipe.
 * @param <C> The cap of the current pipe.
 * @param <S> The SideEffect of the current pipe.
 * @param <M> The current mark'ed type for the current pipe.
 */
public interface Traversal<T, C, S, M> extends Iterator<T>, Iterable<T> {

	/**
	 * Traverse over all the vertices in the graph.
	 * 
	 * @return
	 */
	VertexTraversal<?, ?, M> v();

	/**
	 * Traverse over all the edges in the graph.
	 * 
	 * @return
	 */
	EdgeTraversal<?, ?, M> e();

	/**
	 * Traversal over a list of vertices in the graph.
	 * 
	 * @param ids
	 *            The ids of the vertices.
	 * @return The traversal.
	 */
	VertexTraversal<?, ?, M> v(Object... ids);
	
	/**
	 * Traversal over a list of vertices in the graph.
	 * 
	 * @param ids
	 *            The ids of the vertices.
	 * @return The traversal.
	 */
	VertexTraversal<?, ?, M> v(Collection<?> ids);

	/**
	 * Traverse over all the vertices in the graph that have the specified key
	 * and value.
	 *
	 * @return
	 */
	VertexTraversal<?, ?, M> v(String key, Object value);

	/**
	 * Traversal over a list of edges in the graph.
	 * 
	 * @param ids
	 *            The ids of the edges.
	 * @return The traversal.
	 */
	EdgeTraversal<?, ?, M> e(Object... ids);
	

	/**
	 * Traversal over a list of edges in the graph.
	 * 
	 * @param ids
	 *            The ids of the edges.
	 * @return The traversal.
	 */
	EdgeTraversal<?, ?, M> e(Collection<?> ids);

	/**
	 * Completely drain the pipeline of its objects. Useful when a sideEffect of
	 * the pipeline is desired.
	 */
	void iterate();

	/**
	 * Emit the properties of the incoming element as a java.util.Map.
	 *
	 * @param keys
	 *            the keys to get from the element (if none provided, all keys
	 *            retrieved)
	 * @return the extended Pipeline
	 */
	Traversal<Map<String, Object>, ?, ?, M> map(String... keys);

	/**
	 * Will only emit the object if it has not been seen before.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> dedup();

	/**
	 * Will only emit the object if the object generated by its function hasn't
	 * been seen before.
	 *
	 * @param dedupFunction
	 *            a function to call on the object to yield the object to dedup
	 *            on
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> dedup(TraversalFunction<T, ?> dedupFunction);

	/**
	 * Will only emit the object if it is not in the provided collection.
	 *
	 * @param collection
	 *            the collection except from the stream
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> except(Iterable<?> collection);

	/**
	 * Will only emit the object if it is not equal to any of the objects
	 * contained at the named steps.
	 *
	 * @param namedSteps
	 *            the named steps in the pipeline
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> except(String... namedSteps);

	/**
	 * The serves are an arbitrary filter where the filter criteria is provided
	 * by the filterFunction.
	 *
	 * @param filterFunction
	 *            the filter function of the pipe
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> filter(TraversalFunction<T, Boolean> filterFunction);

	/**
	 * A biased coin toss determines if the object is emitted or not.
	 *
	 * @param bias
	 *            the bias of the random coin
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> random(double bias);

	/**
	 * Analogous to a high/low index lookup. This is an open range so high is inclusive.
	 *
	 * @param low
	 *            the low end of the range
	 * @param high
	 *            the high end of the range
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> range(int low, int high);
	
	
	/**
	 * Limit the number of elements returned.
	 *
	 * @param limit the number of elements to return.
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> limit(int limit);

	/**
	 * Will emit the object only if it is in the provided collection.
	 *
	 * @param collection
	 *            the collection to retain
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> retain(Iterable<?> collection);

	/**
	 * Will only emit the object if it is equal to any of the objects contained
	 * at the named steps.
	 *
	 * @param namedSteps
	 *            the named steps in the pipeline
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> retain(String... namedSteps);

	/**
	 * The objects prior to aggregate are greedily collected into an ArrayList.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, Collection<? extends T>, Collection<? extends T>, M> aggregate();

	/**
	 * The objects prior to aggregate are greedily collected into the provided
	 * collection.
	 *
	 * @param aggregate
	 *            the collection to aggregate results into
	 * @return the extended Pipeline
	 */
	Traversal<T, Collection<? extends T>, Collection<? extends T>, M> aggregate(Collection<? super T> aggregate);

	/**
	 * The results of the function evaluated on the objects prior to the
	 * aggregate are greedily collected into the provided collection.
	 *
	 * @param aggregate
	 *            the collection to aggregate results into
	 * @param aggregateFunction
	 *            the function to run over each object prior to insertion into
	 *            the aggregate
	 * @return the extended Pipeline
	 */
	<N> Traversal<T, Collection<? extends N>, Collection<? extends N>, M> aggregate(Collection<? super N> aggregate, TraversalFunction<T, ? extends N> aggregateFunction);

	/**
	 * The results of the function evaluated on the objects prior to the
	 * aggregate are greedily collected into an ArrayList.
	 *
	 * @param aggregateFunction
	 *            the function to run over each object prior to insertion into
	 *            the aggregate
	 * @return the extended Pipeline
	 */
	<N> Traversal<T, Collection<? extends N>, Collection<? extends N>, M> aggregate(TraversalFunction<T, ? extends N> aggregateFunction);

	/**
	 * Group the objects inputted objects according to a key generated from the
	 * object and a value generated from the object. The grouping map has values
	 * that are Lists.
	 *
	 * @param map
	 *            the map to store the grouping in
	 * @param keyFunction
	 *            the function that generates the key from the object
	 * @param valueFunction
	 *            the function that generates the value from the function
	 * @return the extended Pipeline
	 */
	<K, V> Traversal<T, Map<K, List<V>>, Map<K, List<V>>, M> groupBy(Map<K, List<V>> map,
																	 TraversalFunction<T, K> keyFunction, TraversalFunction<T, Iterator<V>> valueFunction);

	/**
	 * Group the objects inputted objects according to a key generated from the
	 * object and a value generated from the object. The grouping map has values
	 * that are Lists.
	 *
	 * @param keyFunction
	 *            the function that generates the key from the object
	 * @param valueFunction
	 *            the function that generates the value from the function
	 * @return the extended Pipeline
	 */
	<K, V> Traversal<T, Map<K, List<V>>, Map<K, List<V>>, M> groupBy(TraversalFunction<T, K> keyFunction,
																	 TraversalFunction<T, Iterator<V>> valueFunction);

	/**
	 * Group the objects inputted objects according to a key generated from the
	 * object and a value generated from the object. The grouping map has values
	 * that are Lists. When the pipe has no more incoming objects, apply the
	 * reduce function to the keyed Lists. The sideEffect is only fully
	 * available when the pipe is empty.
	 *
	 * @param reduceMap
	 *            a map to perform the reduce operation on (good for having a
	 *            later reference)
	 * @param keyFunction
	 *            the function that generates the key from the object
	 * @param valueFunction
	 *            the function that generates the value from the function
	 * @param reduceFunction
	 *            the function that reduces the value lists
	 * @return the extended Pipeline
	 */
	<K, V, V2> Traversal<T, Map<K, V2>, Map<K, V2>, M> groupBy(Map<K, V2> reduceMap,
															   TraversalFunction<T, K> keyFunction, TraversalFunction<T, Iterator<V>> valueFunction,
															   TraversalFunction<List<V>, V2> reduceFunction);

	/**
	 * Group the objects inputted objects according to a key generated from the
	 * object and a value generated from the object. The grouping map has values
	 * that are Lists. When the pipe has no more incoming objects, apply the
	 * reduce function to the keyed Lists. The sideEffect is only fully
	 * available when the pipe is empty.
	 *
	 * @param keyFunction
	 *            the function that generates the key from the object
	 * @param valueFunction
	 *            the function that generates the value from the function
	 * @param reduceFunction
	 *            the function that reduces the value lists
	 * @return the extended Pipeline
	 */
	<K, V, V2> Traversal<T, Map<K, V2>, Map<K, V2>, M> groupBy(TraversalFunction<T, K> keyFunction,
															   TraversalFunction<T, Iterator<V>> valueFunction, TraversalFunction<List<V>, V2> reduceFunction);

	/**
	 * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the
	 * Pipeline. A map is maintained of counts. The map keys are determined by
	 * the function on the incoming object. The map values are determined by the
	 * function on the incoming object (getA()) and the previous value (getB()).
	 *
	 * @param map
	 *            a provided count map
	 * @param keyFunction
	 *            the key function to determine map key
	 * @param valueFunction
	 *            the value function to determine map value
	 * @return the extended Pipeline
	 */
	<K> Traversal<T, Map<K, Long>, Map<K, Long>, M> groupCount(Map<K, Long> map,
															   TraversalFunction<T, K> keyFunction, TraversalFunction<Pair<T, Long>, Long> valueFunction);

	/**
	 * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the
	 * Pipeline. map is maintained of counts. The map keys are determined by the
	 * function on the incoming object. The map values are determined by the
	 * function on the incoming object (getA()) and the previous value (getB()).
	 *
	 * @param keyFunction
	 *            the key function to determine map key
	 * @param valueFunction
	 *            the value function to determine map value
	 * @return the extended Pipeline
	 */
	<K> Traversal<T, Map<K, Long>, Map<K, Long>, M> groupCount(TraversalFunction<T, K> keyFunction,
															   TraversalFunction<Pair<T, Long>, Long> valueFunction);

	/**
	 * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the
	 * Pipeline. A map is maintained of counts. The map keys are determined by
	 * the function on the incoming object. The map values are 1 plus the
	 * previous value for that key.
	 *
	 * @param map
	 *            a provided count map
	 * @param keyFunction
	 *            the key function to determine map key
	 * @return the extended Pipeline
	 */
	<K> Traversal<T, Map<K, Long>, Map<K, Long>, M> groupCount(Map<K, Long> map,
															   TraversalFunction<T, K> keyFunction);

	/**
	 * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the
	 * Pipeline. A map is maintained of counts. The map keys are determined by
	 * the function on the incoming object. The map values are 1 plus the
	 * previous value for that key.
	 *
	 * @param keyFunction
	 *            the key function to determine map key
	 * @return the extended Pipeline
	 */
	<K> Traversal<T, Map<K, Long>, Map<K, Long>, M> groupCount(TraversalFunction<T, K> keyFunction);

	/**
	 * A map is maintained of counts. The map keys are the incoming objects. The
	 * map values are 1 plus the previous value for that key.
	 *
	 * @param map
	 *            a provided count map
	 * @return the extended Pipeline
	 */
	Traversal<T, Map<T, Long>, Map<T, Long>, M> groupCount(Map<T, Long> map);

	/**
	 * A map is maintained of counts. The map keys are the incoming objects. The
	 * map values are 1 plus the previous value for that key.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, Map<T, Long>, Map<T, Long>, M> groupCount();

	/**
	 * The provided function is evaluated and the incoming object is the
	 * outgoing object.
	 *
	 * @param sideEffectFunction
	 *            the function of the pipe
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> sideEffect(SideEffectFunction<T> sideEffectFunction);

	/**
	 * Lazily store the incoming objects into the provided collection.
	 *
	 * @param storage
	 *            the collection to store results into
	 * @return the extended Pipeline
	 */
	<N> Traversal<T, Collection<N>, N, M> store(Collection<N> storage);

	/**
	 * Lazily store the object returned by the function over the incoming object
	 * into the provided collection.
	 *
	 * @param storage
	 *            the collection to store results into
	 * @param storageFunction
	 *            the function to run over each object prior to insertion into
	 *            the storage collection
	 * @return the extended Pipeline
	 */
	<N> Traversal<T, Collection<N>, N, M> store(Collection<N> storage, TraversalFunction<T, N> storageFunction);

	/**
	 * An ArrayList storage collection is provided and filled lazily with
	 * incoming objects.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, Collection<T>, T, M> store();

	/**
	 * An ArrayList storage collection is provided and filled lazily with the
	 * return of the function evaluated over the incoming objects.
	 *
	 * @param storageFunction
	 *            the function to run over each object prior to insertion into
	 *            the storage collection
	 * @return the extended Pipeline
	 */
	<N> Traversal<T, Collection<N>, N, M> store(TraversalFunction<T, N> storageFunction);

	/**
	 * This step is used for grabbing previously seen objects the pipeline as
	 * identified by as-steps.
	 *
	 * @param table
	 *            the table to fill
	 * @param stepNames
	 *            the partition steps to include in the filling
	 * @param columnFunctions
	 *            the post-processing function for each column
	 * @return the extended Pipeline
	 */
	Traversal<T, Table, Table, M> table(Table table, Collection<String> stepNames,
										TraversalFunction<?, ?>... columnFunctions);

	/**
	 * This step is used for grabbing previously seen objects the pipeline as
	 * identified by as-steps.
	 *
	 * @param table
	 *            the table to fill
	 * @param columnFunctions
	 *            the post-processing function for each column
	 * @return the extended Pipeline
	 */
	Traversal<T, Table, Table, M> table(Table table, TraversalFunction<?, ?>... columnFunctions);

	/**
	 * This step is used for grabbing previously seen objects the pipeline as
	 * identified by as-steps.
	 *
	 * @param columnFunctions
	 *            the post-processing function for each column
	 * @return the extended Pipeline
	 */
	Traversal<T, Table, Table, M> table(TraversalFunction<?, ?>... columnFunctions);

	/**
	 * This step is used for grabbing previously seen objects the pipeline as
	 * identified by as-steps.
	 *
	 * @param table
	 *            the table to fill
	 * @return the extended Pipeline
	 */
	Traversal<T, Table, Table, M> table(Table table);

	/**
	 * This step is used for grabbing previously seen objects the pipeline as
	 * identified by as-steps.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, Table, Table, M> table();

	/**
	 * Add a TreePipe to the end of the Pipeline This step maintains an internal
	 * tree representation of the paths that have flowed through the step.
	 *
	 * @param tree
	 *            an embedded Map data structure to store the tree
	 *            representation in
	 * @return the extended Pipeline
	 */
	<N> Traversal<T, Tree<N>, Tree<N>, M> tree(Tree<N> tree);

	/**
	 * Add a TreePipe to the end of the Pipeline This step maintains an internal
	 * tree representation of the paths that have flowed through the step.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, Tree<T>, Tree<T>, M> tree();

	/**
	 * Useful in various situations where a step is needed without processing.
	 * For example, useful when two as-steps are needed in a row.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> identity();

	/**
	 * This step will hold a Map of the objects that have entered into its
	 * pipeline section. If an input is seen twice, then the map stored output
	 * is emitted instead of recomputing the pipeline section.
	 *
	 * @param namedStep
	 *            the name of the step previous to memoize to
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> memoize(String namedStep);

	/**
	 * This step will hold a Map of the objects that have entered into its
	 * pipeline section. If an input is seen twice, then the map stored output
	 * is emitted instead of recomputing the pipeline section.
	 *
	 * @param namedStep
	 *            the name of the step previous to memoize to
	 * @param map
	 *            the memoization map
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> memoize(String namedStep, Map<?, ?> map);

	/**
	 * This step will sort the objects in the stream in a default Comparable
	 * order.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> order();

	/**
	 * This step will sort the objects in the stream in a default Comparable
	 * order.
	 *
	 * @param order
	 *            if the stream is composed of comparable objects, then
	 *            increment or decrement can be specified
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> order(TransformPipe.Order order);

	/**
	 * This step will sort the objects in the stream in a default Comparable
	 * order.
	 *
	 * @param order
	 *            if the stream is composed of comparable objects, then
	 *            increment or decrement can be specified
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> order(Tokens.T order);

	/**
	 * This step will sort the objects in the stream according to a comparator
	 * defined in the provided function.
	 *
	 * @param compareFunction
	 *            a comparator function of two objects of type e
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> order(Comparator<T> compareFunction);

	/**
	 * Wrap the previous step in an AsPipe. Useful for naming steps and is used
	 * in conjunction with various other steps including: loop, select, back,
	 * table, etc.
	 *
	 * @param name
	 *            the name of the AsPipe
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> as(String name);

	/**
	 * If the object's path is repeating (looping), then the object is filtered.
	 * Thus, what is emitted are those objects whose history is composed of
	 * unique objects.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> simplePath();

	/**
	 * The object that was seen at the topmost marked step is emitted. The mark step is removed from the stack.
	 *
	 * @return the extended Pipeline
	 */
	M back();

	/**
	 * Marks the step so that a subsequent call to back() or optional() may
	 * return to this point. If the pipeline is a stack then each call to back
	 * or optional will pop all steps back to and including the previous mark.
	 * The next mark in the pipeline is thus exposed.
	 * 
	 * @return the extended Pipeline
	 */
	Traversal<T, C, S, ? extends Traversal<T, C, S, M>> mark();

	/**
	 * Causes the pipeline to be greedy up to this step.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> gatherScatter();

	/**
	 * This will emit the path that has been seen thus far. If path functions
	 * are provided, then they are evaluated in a round robin fashion on the
	 * objects of the path.
	 *
	 * @param pathFunctions
	 *            the path function of the PathPipe
	 * @return the extended Pipeline
	 */
	Traversal<Path, ? , ?, M> path(TraversalFunction<?, ?>... pathFunctions);

	/**
	 * The objects of the named steps (via as) previous in the pipeline are
	 * emitted as a Row object. A Row object extends ArrayList and simply
	 * provides named columns and some helper methods. If column functions are
	 * provided, then they are evaluated in a round robin fashion on the objects
	 * of the Row.
	 *
	 * @param stepNames
	 *            the name of the steps in the expression to retrieve the
	 *            objects from
	 * @param columnFunctions
	 *            the functions to apply to the column objects prior to filling
	 *            the Row
	 * @return the extended Pipeline
	 */
	Traversal<Row<?>, ?, ?, M> select(Collection<String> stepNames, TraversalFunction<?, ?>... columnFunctions);

	/**
	 * The objects of the named steps (via as) previous in the pipeline are
	 * emitted as a Row object. A Row object extends ArrayList and simply
	 * provides named columns and some helper methods. If column functions are
	 * provided, then they are evaluated in a round robin fashion on the objects
	 * of the Row.
	 *
	 * @param columnFunctions
	 *            the functions to apply to the column objects prior to filling
	 *            the Row
	 * @return the extended Pipeline
	 */
	Traversal<Row<?>, ?, ?, M> select(TraversalFunction<?, ?>... columnFunctions);

	/**
	 * The objects of the named steps (via as) previous in the pipeline are
	 * emitted as a Row object. A Row object extends ArrayList and simply
	 * provides named columns and some helper methods.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<Row<?>, ?, ?, M> select();

	/**
	 * All the objects previous to this step are aggregated in a greedy fashion,
	 * their order randomized and emitted.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> shuffle();

	/**
	 * When the previous step in the pipeline is implements SideEffectPipe, then
	 * it has a method called getSideEffect(). The cap step will greedily
	 * iterate the pipeline and then, when its empty, emit the side effect of
	 * the previous pipe.
	 *
	 * @return the extended Pipeline
	 */
	C cap();

	/**
	 * This step emits the input but also calls the sideEffectFunction
	 * function with the side effect of the previous step when it is ready.
	 *
	 * @return the extended Pipeline
	 */
	Traversal<T, ?, ?, M> divert(SideEffectFunction<S> sideEffectFunction);

	/**
	 * Given an input, the provided function is computed on the input and the
	 * output of that function is emitted.
	 *
	 * @param function
	 *            the transformation function of the pipe
	 * @return the extended Pipeline
	 */
	<N> Traversal<? extends N, ?, ?, M> transform(TraversalFunction<T, N> function);

	/**
	 * Though, in practice, a StartPipe is usually the beginning. Moreover, the
	 * constructor of the Pipeline will internally use StartPipe.
	 *
	 * @param object
	 *            the object that serves as the start of the pipeline
	 *            (iterator/iterable are unfolded)
	 * @return the extended Pipeline
	 */
	<N> Traversal<N, ?, ?, M> start(N object);

	/**
	 * Though, in practice, a StartPipe is usually the beginning. Moreover, the
	 * constructor of the Pipeline will internally use StartPipe.
	 *
	 * @param object
	 *            the object that serves as the start of the pipeline
	 *            (iterator/iterable are unfolded)
	 * @return the extended Pipeline
	 */
	VertexTraversal<?, ?, M> start(VertexFrame object);

	/**
	 * Though, in practice, a StartPipe is usually the beginning. Moreover, the
	 * constructor of the Pipeline will internally use StartPipe.
	 *
	 * @param object
	 *            the object that serves as the start of the pipeline
	 *            (iterator/iterable are unfolded)
	 * @return the extended Pipeline
	 */
	EdgeTraversal<?, ?, M> start(EdgeFrame object);

	/**
	 * Emit the respective property of the incoming element.
	 *
	 * @param key
	 *            the property key
	 * @return the extended Pipeline
	 */
	<N> Traversal<N, ?, ?, M> property(String key);

	/**
	 * Emit the respective property of the incoming element.
	 *
	 * @param key
	 *            the property key
	 * @param type
	 *            the property type;
	 * @return the extended Pipeline
	 */
	<N> Traversal<? extends N, ?, ?, M> property(String key, Class<N> type);

	/**
	 * Return the number of objects iterated through the pipeline.
	 *
	 * @return the number of objects iterated
	 */
	long count();

	/**
	 * Return the next object in the pipeline.
	 *
	 */
	T next();

	/**
	 * Return the next object in the pipeline.
	 *
	 */
	T nextOrDefault(T defaultValue);

	/**
	 * Return the next X objects in the pipeline as a list.
	 *
	 * @param number
	 *            the number of objects to return
	 * @return a list of X objects (if X objects occur)
	 */
	List<? extends T> next(int number);

	/**
	 * Return a list of all the objects in the pipeline.
	 *
	 * @return a list of all the objects
	 */
	List<? extends T> toList();

	/**
	 * Return a set of all the objects in the pipeline.
	 *
	 * @return a set of all the objects
	 */
	Set<? extends T> toSet();

	/**
	 * Enable path calculations within the Pipeline. This is typically done
	 * automatically and in rare occasions needs to be called.
	 *
	 * @return the Pipeline with path calculations enabled
	 */
	Traversal<T, C, S, M> enablePath();

	/**
	 * When possible, Gremlin takes advantage of certain sequences of pipes in
	 * order to make a more concise, and generally more efficient expression.
	 * This method will turn on and off query optimization from this stage in
	 * the pipeline on.
	 *
	 * @param optimize
	 *            whether to optimize the pipeline from here on or not
	 * @return The GremlinPipeline with the optimization turned off
	 */
	Traversal<T, C, S, M> optimize(boolean optimize);

	/**
	 * Fill the provided collection with the objects in the pipeline.
	 *
	 * @param collection
	 *            the collection to fill
	 * @return the collection filled
	 */
	Collection<T> fill(Collection<? super T> collection);

	/**
	 * The section of pipeline back to the topmost marked step is evaluated. The mark is removed from the stack.
	 *
	 * @return the extended Pipeline
	 */
	M optional();

	/**
	 * Emit the edges of the graph whose ids are those of the incoming id
	 * objects.
	 *
	 * @param graph
	 *            the graph of the pipe
	 * @return the extended Pipeline
	 */
	EdgeTraversal<?, ?, M> idEdge(Graph graph);

	/**
	 * Emit the vertices of the graph whose ids are those of the incoming id
	 * objects.
	 *
	 * @param graph
	 *            the graph of the pipe
	 * @return the extended Pipeline
	 */
	VertexTraversal<?, ?, M> idVertex(Graph graph);

	/**
	 * Emit the ids of the incoming objects.
	 *
	 * @return A traversal of the ids.
	 * @since 2.1.0
	 */
	<N> Traversal<N, ?, ?, M> id();

	/**
	 * Emit the ids of the incoming objects, cast to the specified class.
	 *
	 * @param c the class type to cast the ids to.
	 * @return A traversal of the ids.
	 * @since 2.1.0
	 */
	<N> Traversal<? extends N, ?, ?, M> id(Class<N> c);
}