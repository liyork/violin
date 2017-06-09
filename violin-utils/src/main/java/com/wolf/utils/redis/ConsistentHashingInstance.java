package com.wolf.utils.redis;


import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash算法类
 *  线程不安全
 * <br/> Created on 2014-1-21 下午4:49:19

 * @since 3.3
 */
public final class ConsistentHashingInstance {
	//默认虚节点个数
	public static final int NUMREPS = 160;
	
	public static final int HASH_FOUR = 4;
	
	private TreeMap<Long, Node> ketamaNodes;
	private HashAlgorithm hashAlg;
	private int numReps ;
	
	public ConsistentHashingInstance(List<Node> nodes, HashAlgorithm alg){
		this(nodes, alg, NUMREPS);
	}
	
    public ConsistentHashingInstance(List<Node> nodes, HashAlgorithm alg, int nodeCopies) {
		hashAlg = alg;
		ketamaNodes = new TreeMap<Long, Node>();
		
        numReps= nodeCopies;
        
		for (Node node : nodes) {
			for (int i = 0; i < numReps / HASH_FOUR; i++) {
				byte[] digest = hashAlg.computeMd5(node.getName() + i);
				for(int h = 0; h < HASH_FOUR; h++) {
					long m = hashAlg.hash(digest, h);
					
					ketamaNodes.put(m, node);
				}
			}
		}
    }
    /**
     * 获取结点信息
     * 
     * <br/> Created on 2014-2-13 下午4:13:00

     * @since 3.3
     * @param k
     * @return
     */
	public Node getPrimary(final String k) {
		byte[] digest = hashAlg.computeMd5(k);
		Node rv = getNodeForKey(hashAlg.hash(digest, 0));
		return rv;
	}

	Node getNodeForKey(long hash) {
		final Node rv;
		Long key = hash;
		if(!ketamaNodes.containsKey(key)) {
			SortedMap<Long, Node> tailMap=ketamaNodes.tailMap(key);
			if(tailMap.isEmpty()) {
				key=ketamaNodes.firstKey();
			} else {
				key=tailMap.firstKey();
			}
			//For JDK1.6 version
//			key = ketamaNodes.ceilingKey(key);
//			if (key == null) {
//				key = ketamaNodes.firstKey();
//			}
		}
		
		
		rv=ketamaNodes.get(key);
		return rv;
	}
}
