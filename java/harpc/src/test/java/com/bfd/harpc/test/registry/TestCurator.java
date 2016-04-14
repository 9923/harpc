/**  
 * @author ZHIQIANG ZHOU  
 * @since 1.0.0
 */
package com.bfd.harpc.test.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.data.Stat;

/**
 *
 *
 *
 * @author ZHIQIANG ZHOU
 */
public class TestCurator {
	private static CuratorFramework client = TestCurator.newClient();  
	private static final String PATH = "/crud"; 
	public static void main(String[] args) {
		try {  
            client.start();  
  
            client.create().forPath(PATH, "I love messi".getBytes());  
  
            byte[] bs = client.getData().forPath(PATH);  
            System.out.println("新建的节点，data为:" + new String(bs));  
  
            client.setData().forPath(PATH, "I love football".getBytes());  
            
            
            System.out.println(client.getNamespace());
            
            // 由于是在background模式下获取的data，此时的bs可能为null  
            byte[] bs2 = client.getData().watched().inBackground().forPath(PATH);  
            System.out.println("修改后的data为" + new String(bs2 != null ? bs2 : new byte[0]));  
  
            client.delete().forPath(PATH);  
            Stat stat = client.checkExists().forPath(PATH);  
  
            // Stat就是对zonde所有属性的一个映射， stat=null表示节点不存在！  
            System.out.println(stat);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            CloseableUtils.closeQuietly(client);  
        }  
	}
	
	public static CuratorFramework newClient(){
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
		return builder.connectString("127.0.0.1:2181")  
		        .sessionTimeoutMs(30000)  
		        .connectionTimeoutMs(30000)  
		        .canBeReadOnly(false)  
		        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))  
		        .namespace("zzx")  
		        .defaultData(null)  
		        .build();  
	}
}
