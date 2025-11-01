package com.wetech.demo.web3j.service;

import com.wetech.demo.web3j.contracts.erc20test.ERC20Test;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ERC20Service {

    private final Web3j web3j;
    private final Credentials credentials;
    private final ContractGasProvider gasProvider;

    private ERC20Test contract;

    /**
     * -- GETTER --
     * Get the address of the currently loaded contract
     *
     * @return the contract address
     */
    @Getter
    private String contractAddress;

    /**
     * Deploy the ERC20Test contract to the blockchain
     * @return the address of the deployed contract
     */
    public CompletableFuture<String> deployContract() {
        log.info("Deploying ERC20Test contract...");
        return ERC20Test.deploy(web3j, credentials, gasProvider)
                .sendAsync()
                .thenApply(contract -> {
                    this.contract = contract;
                    this.contractAddress = contract.getContractAddress();
                    log.info("ERC20Test contract deployed to: {}", contractAddress);
                    return contractAddress;
                });
    }

    /**
     * Load an existing contract from the blockchain
     * @param contractAddress the address of the contract to load
     */
    public void loadContract(String contractAddress) {
        log.info("Loading ERC20Test contract from address: {}", contractAddress);
        this.contract = ERC20Test.load(contractAddress, web3j, credentials, gasProvider);
        this.contractAddress = contractAddress;
    }

    /**
     * Mint new tokens
     * @param to the address to mint tokens to
     * @param amount the amount of tokens to mint
     * @return the transaction receipt
     */
    public CompletableFuture<TransactionReceipt> mint(String to, BigInteger amount) {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Minting {} tokens to address: {}", amount, to);
        return contract.mint(to, amount).sendAsync();
    }

    /**
     * Transfer tokens to another address
     * @param to the recipient address
     * @param amount the amount of tokens to transfer
     * @return the transaction receipt
     */
    public CompletableFuture<TransactionReceipt> transfer(String to, BigInteger amount) {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Transferring {} tokens to address: {}", amount, to);
        return contract.transfer(to, amount).sendAsync();
    }

    /**
     * Get the token balance of an address
     * @param owner the address to check balance for
     * @return the token balance
     */
    public CompletableFuture<BigInteger> balanceOf(String owner) {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Getting balance for address: {}", owner);
        return contract.balanceOf(owner).sendAsync();
    }

    /**
     * Approve another address to spend tokens on behalf of the caller
     * @param spender the address that is allowed to spend tokens
     * @param amount the amount of tokens that can be spent
     * @return the transaction receipt
     */
    public CompletableFuture<TransactionReceipt> approve(String spender, BigInteger amount) {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Approving {} tokens for address: {}", amount, spender);
        return contract.approve(spender, amount).sendAsync();
    }

    /**
     * Transfer tokens from one address to another (requires prior approval)
     * @param from the address to transfer tokens from
     * @param to the address to transfer tokens to
     * @param amount the amount of tokens to transfer
     * @return the transaction receipt
     */
    public CompletableFuture<TransactionReceipt> transferFrom(String from, String to, BigInteger amount) {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Transferring {} tokens from {} to {}", amount, from, to);
        return contract.transferFrom(from, to, amount).sendAsync();
    }

    /**
     * Get the amount of tokens that an owner has approved for a spender
     * @param owner the address that owns the tokens
     * @param spender the address that is allowed to spend tokens
     * @return the approved amount
     */
    public CompletableFuture<BigInteger> allowance(String owner, String spender) {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Getting allowance for owner: {} spender: {}", owner, spender);
        return contract.allowance(owner, spender).sendAsync();
    }

    /**
     * Get the total supply of tokens
     * @return the total supply
     */
    public CompletableFuture<BigInteger> totalSupply() {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Getting total supply");
        return contract.totalSupply().sendAsync();
    }

    /**
     * Get the token name
     * @return the token name
     */
    public CompletableFuture<String> name() {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Getting token name");
        return contract.name().sendAsync();
    }

    /**
     * Get the token symbol
     * @return the token symbol
     */
    public CompletableFuture<String> symbol() {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Getting token symbol");
        return contract.symbol().sendAsync();
    }

    /**
     * Get the token decimals
     * @return the token decimals
     */
    public CompletableFuture<BigInteger> decimals() {
        if (contract == null) {
            throw new IllegalStateException("Contract not deployed or loaded");
        }
        log.info("Getting token decimals");
        return contract.decimals().sendAsync();
    }
}