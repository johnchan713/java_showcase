package com.example.demo.showcase;

/**
 * Demonstrates BitcoinJ - Bitcoin Library for Java
 * Covers wallets, transactions, and blockchain interaction
 */
public class BitcoinJShowcase {

    public static void demonstrate() {
        System.out.println("\n========== BITCOINJ SHOWCASE ==========\n");

        System.out.println("--- BitcoinJ Overview ---");
        System.out.println("Bitcoin protocol implementation in Java\n");

        System.out.println("Key features:");
        System.out.println("   • HD Wallets (BIP32/BIP39/BIP44)");
        System.out.println("   • Transaction creation and signing");
        System.out.println("   • SPV (Simplified Payment Verification)");
        System.out.println("   • Payment channels");
        System.out.println("   • Testnet support");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.bitcoinj</groupId>
                <artifactId>bitcoinj-core</artifactId>
                <version>0.16.2</version>
            </dependency>
            """);

        System.out.println("\n--- Network Setup ---");
        System.out.println("""
            import org.bitcoinj.core.*;
            import org.bitcoinj.params.*;
            import org.bitcoinj.wallet.*;
            
            // Network parameters
            NetworkParameters params = TestNet3Params.get();  // Testnet
            // NetworkParameters params = MainNetParams.get();  // Mainnet
            
            // Create wallet
            Wallet wallet = Wallet.createDeterministic(params, ScriptType.P2PKH);
            
            // Get address
            Address address = wallet.currentReceiveAddress();
            System.out.println("Address: " + address);
            """);

        System.out.println("\n--- HD Wallet (BIP44) ---");
        System.out.println("""
            import org.bitcoinj.crypto.*;
            
            // Generate mnemonic (12 words)
            DeterministicSeed seed = new DeterministicSeed(
                new SecureRandom(),
                128,  // 12 words
                ""
            );
            
            List<String> mnemonicWords = seed.getMnemonicCode();
            System.out.println("Mnemonic: " + String.join(" ", mnemonicWords));
            
            // Restore from mnemonic
            DeterministicSeed restoredSeed = new DeterministicSeed(
                mnemonicWords,
                null,
                "",
                seed.getCreationTimeSeconds()
            );
            
            Wallet wallet = Wallet.fromSeed(params, restoredSeed, ScriptType.P2PKH);
            
            // Derive keys
            DeterministicKeyChain keyChain = DeterministicKeyChain.builder()
                .seed(seed)
                .build();
            
            DeterministicKey key = keyChain.getKey(KeyChain.KeyPurpose.RECEIVE_FUNDS);
            """);

        System.out.println("\n--- Sending Bitcoin ---");
        System.out.println("""
            // Create transaction
            Address toAddress = Address.fromString(params, "tb1q...");
            Coin amount = Coin.parseCoin("0.001");  // 0.001 BTC
            
            SendRequest sendRequest = SendRequest.to(toAddress, amount);
            sendRequest.feePerKb = Coin.valueOf(1000);  // Fee
            
            // Complete and sign
            wallet.completeTx(sendRequest);
            
            Transaction tx = sendRequest.tx;
            System.out.println("Transaction: " + tx.getTxId());
            
            // Broadcast
            // peerGroup.broadcastTransaction(tx);
            """);

        System.out.println("\n--- Receiving Bitcoin ---");
        System.out.println("""
            // Listen for coins received
            wallet.addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {
                System.out.println("Received transaction!");
                System.out.println("Previous balance: " + prevBalance.toFriendlyString());
                System.out.println("New balance: " + newBalance.toFriendlyString());
                System.out.println("Transaction: " + tx.getTxId());
            });
            
            // Get balance
            Coin balance = wallet.getBalance();
            System.out.println("Balance: " + balance.toFriendlyString());
            
            // Available balance (confirmed)
            Coin available = wallet.getBalance(Wallet.BalanceType.AVAILABLE);
            """);

        System.out.println("\n--- Transaction Details ---");
        System.out.println("""
            // Get transaction history
            Set<Transaction> transactions = wallet.getTransactions(false);
            
            for (Transaction tx : transactions) {
                System.out.println("TX ID: " + tx.getTxId());
                System.out.println("Confidence: " + tx.getConfidence().getDepthInBlocks());
                System.out.println("Value: " + tx.getValue(wallet).toFriendlyString());
                System.out.println("Fee: " + tx.getFee());
                System.out.println("Time: " + tx.getUpdateTime());
                
                // Inputs
                for (TransactionInput input : tx.getInputs()) {
                    System.out.println("Input: " + input.getOutpoint());
                }
                
                // Outputs
                for (TransactionOutput output : tx.getOutputs()) {
                    System.out.println("Output: " + output.getValue().toFriendlyString());
                    System.out.println("To: " + output.getScriptPubKey().getToAddress(params));
                }
            }
            """);

        System.out.println("\n--- SPV (Bloom Filters) ---");
        System.out.println("""
            import org.bitcoinj.net.discovery.*;
            import org.bitcoinj.store.*;
            
            // Block store
            BlockStore blockStore = new SPVBlockStore(params, 
                new File("spv.blockchain"));
            
            // Blockchain
            BlockChain chain = new BlockChain(params, wallet, blockStore);
            
            // Peer group
            PeerGroup peerGroup = new PeerGroup(params, chain);
            peerGroup.addPeerDiscovery(new DnsDiscovery(params));
            peerGroup.addWallet(wallet);
            
            // Start
            peerGroup.start();
            peerGroup.downloadBlockChain();
            
            // Wait for sync
            peerGroup.waitForPeersOfVersion(1, 70000);
            
            // Shutdown
            peerGroup.stop();
            blockStore.close();
            """);

        System.out.println("\n--- Coin Selection ---");
        System.out.println("""
            // Default coin selector (largest first)
            sendRequest.coinSelector = wallet.getCoinSelector();
            
            // Custom coin selector
            sendRequest.coinSelector = new CoinSelector() {
                @Override
                public CoinSelection select(Coin target, List<TransactionOutput> candidates) {
                    // Custom selection logic
                    return new CoinSelection(target, candidates);
                }
            };
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use testnet for development");
        System.out.println("   ✓ Backup mnemonic words securely");
        System.out.println("   ✓ Set appropriate fees");
        System.out.println("   ✓ Wait for confirmations");
        System.out.println("   ✓ Handle reorgs properly");
        System.out.println("   ✗ Never expose private keys");
        System.out.println();
    }
}
