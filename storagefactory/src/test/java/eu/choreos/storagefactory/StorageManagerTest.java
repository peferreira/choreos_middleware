package eu.choreos.storagefactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import eu.choreos.storagefactory.datamodel.StorageNode;
import eu.choreos.storagefactory.datamodel.StorageNodeSpec;

public class StorageManagerTest {
	protected StorageNodeManager storageManager;
	protected StorageNodeSpec spec1 = new StorageNodeSpec();
	protected StorageNodeSpec spec2 = new StorageNodeSpec();

	@Before
	public void setUp() throws Exception {

		storageManager = new StorageNodeManager();

		spec1.setUuid("1");
		spec1.setType("mysql");

		spec2.setUuid("2");
		spec2.setType("mysql");
	}

	@Test
	public void shouldCreateAndStoreNodeDescription() throws Exception {
		StorageNode instantiatedNode = storageManager
				.registerNewStorageNode(spec1);

		assertEquals(spec1.getUuid(), instantiatedNode.getUuid());
		assertEquals(spec1.getType(), instantiatedNode.getType());
	}

	@Test
	public void shouldGetAnStorageNodeByItsID() throws Exception {

		storageManager.registerNewStorageNode(spec1);
		storageManager.registerNewStorageNode(spec2);

		assertSame(spec2.getUuid(), storageManager.registry.getNode("2")
				.getUuid());
		assertSame(spec1.getUuid(), storageManager.registry.getNode("1")
				.getUuid());
	}

	@Test
	public void shouldGetAllStorageNodes() throws Exception {

		storageManager.registerNewStorageNode(spec1);
		storageManager.registerNewStorageNode(spec2);

		assertEquals(2, storageManager.registry.getNodes().size());
	}

	@Test
	public void shouldAddRemoveAndKeepCountOfNodes() throws Exception {
		storageManager.registerNewStorageNode(spec1);
		assertEquals(1, storageManager.registry.getNodes().size());

		storageManager.registerNewStorageNode(spec2);
		assertEquals(2, storageManager.registry.getNodes().size());

		storageManager.destroyNode("2");
		assertEquals(1, storageManager.registry.getNodes().size());

		storageManager.destroyNode("1");
		assertEquals(0, storageManager.registry.getNodes().size());
	}

	@Test
	public void shouldAddAndRemoveSingleNode() throws Exception {

		assertEquals(0, storageManager.registry.getNodes().size());

		storageManager.registerNewStorageNode(spec1);
		assertEquals(1, storageManager.registry.getNodes().size());
		assertSame(spec1.getUuid(), storageManager.registry.getNode("1")
				.getUuid());

		storageManager.destroyNode("1");

		assertEquals(0, storageManager.registry.getNodes().size());

	}
}