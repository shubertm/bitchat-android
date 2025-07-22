package com.bitchat

import com.bitchat.android.mesh.PeerManager
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test


class PeerManagerTest {

    private val peerManager = PeerManager()

    val testUsers = mapOf(
        "peer1" to "alice",
        "peer2" to "bob",
        "peer3" to "charlie",
        "peer4" to "diana",
        "peer5" to "eve"
    )

    val testRSSI = mapOf(
        "peer1" to 0,
        "peer2" to 10,
        "peer3" to 30,
        "peer4" to 5,
        "peer5" to 25
    )

    @Test
    fun peer_is_added_correctly() {
        testUsers.forEach { peerID, nickname ->
            assertTrue(peerManager.addOrUpdatePeer(peerID, nickname))
        }
    }

    @Test
    fun all_peer_nicknames_are_returned_correctly() {
        peer_is_added_correctly()
        assertEquals(testUsers, peerManager.getAllPeerNicknames())
    }

    @Test
    fun peer_is_removed_correctly() {
        peer_is_added_correctly()
        peerManager.removePeer(testUsers.keys.elementAt(0))
        peerManager.removePeer(testUsers.keys.elementAt(1))
        assertEquals(testUsers.size - 2, peerManager.getActivePeerCount())
    }

    @Test
    fun peer_is_already_removed() {
        peer_is_added_correctly()
        peerManager.removePeer(testUsers.keys.elementAt(0))
        peerManager.removePeer(testUsers.keys.elementAt(0))
        assertEquals(testUsers.size - 1, peerManager.getActivePeerCount())
    }


    @Test
    fun last_seen_updated_correctly() {
        testUsers.forEach { peerID, _ ->
            peerManager.updatePeerLastSeen(peerID)
        }
    }

    @Test
    fun rssi_updated_correctly() {
        peer_is_added_correctly()
        testRSSI.forEach { peerID, rssi ->
            peerManager.updatePeerRSSI(peerID, rssi)
        }
        assertEquals(testRSSI, peerManager.getAllPeerRSSI())
    }

    @Test
    fun peer_can_be_marked_as_announced_correctly() {
        peer_is_added_correctly()
        testUsers.forEach { peerID, _ ->
            peerManager.markPeerAsAnnouncedTo(peerID)
            assertEquals(true, peerManager.hasAnnouncedToPeer(peerID))
        }
    }

    @Test
    fun peer_can_announce_correctly() {
        peer_is_added_correctly()
        testUsers.forEach { peerID, _ ->
            assertEquals(true, peerManager.isPeerActive(peerID))
        }
    }

    @Test
    fun all_peers_cleared_correctly() {
        peer_is_added_correctly()
        assertEquals(true, peerManager.getAllPeerNicknames().isNotEmpty())
        peerManager.clearAllPeers()
        assertEquals(true, peerManager.getAllPeerNicknames().isEmpty())
    }

    @Test
    fun peer_manager_can_shutdown_properly() {
        peer_is_added_correctly()
        assertEquals(true, peerManager.getAllPeerNicknames().isNotEmpty())
        peerManager.shutdown()
        assertEquals(true, peerManager.getAllPeerNicknames().isEmpty())
    }
}