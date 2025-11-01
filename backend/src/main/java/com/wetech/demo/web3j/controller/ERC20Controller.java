package com.wetech.demo.web3j.controller;

import com.wetech.demo.web3j.service.ERC20Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/erc20")
@RequiredArgsConstructor
public class ERC20Controller {

    private final ERC20Service erc20Service;

    /**
     * Deploy a new ERC20 contract
     * @return the address of the deployed contract
     */
    @PostMapping("/deploy")
    public CompletableFuture<ResponseEntity<Map<String, String>>> deployContract() {
        return erc20Service.deployContract()
                .thenApply(address -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("contractAddress", address);
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Load an existing contract
     * @param address the address of the contract to load
     * @return a success message
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadContract(@RequestParam String address) {
        erc20Service.loadContract(address);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Contract loaded successfully");
        response.put("contractAddress", address);
        return ResponseEntity.ok(response);
    }

    /**
     * Mint new tokens
     * @param to the address to mint tokens to
     * @param amount the amount of tokens to mint
     * @return the transaction receipt details
     */
    @PostMapping("/mint")
    public CompletableFuture<ResponseEntity<Map<String, String>>> mint(
            @RequestParam String to,
            @RequestParam String amount) {
        BigInteger bigAmount = new BigInteger(amount);
        return erc20Service.mint(to, bigAmount)
                .thenApply(receipt -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("transactionHash", receipt.getTransactionHash());
                    response.put("blockNumber", receipt.getBlockNumber().toString());
                    response.put("gasUsed", receipt.getGasUsed().toString());
                    response.put("status", receipt.getStatus());
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Transfer tokens to another address
     * @param to the recipient address
     * @param amount the amount of tokens to transfer
     * @return the transaction receipt details
     */
    @PostMapping("/transfer")
    public CompletableFuture<ResponseEntity<Map<String, String>>> transfer(
            @RequestParam String to,
            @RequestParam String amount) {
        BigInteger bigAmount = new BigInteger(amount);
        return erc20Service.transfer(to, bigAmount)
                .thenApply(receipt -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("transactionHash", receipt.getTransactionHash());
                    response.put("blockNumber", receipt.getBlockNumber().toString());
                    response.put("gasUsed", receipt.getGasUsed().toString());
                    response.put("status", receipt.getStatus());
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Get the token balance of an address
     * @param owner the address to check balance for
     * @return the token balance
     */
    @GetMapping("/balance/{owner}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> balanceOf(@PathVariable String owner) {
        return erc20Service.balanceOf(owner)
                .thenApply(balance -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("balance", balance.toString());
                    response.put("owner", owner);
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Approve another address to spend tokens on behalf of the caller
     * @param spender the address that is allowed to spend tokens
     * @param amount the amount of tokens that can be spent
     * @return the transaction receipt details
     */
    @PostMapping("/approve")
    public CompletableFuture<ResponseEntity<Map<String, String>>> approve(
            @RequestParam String spender,
            @RequestParam String amount) {
        BigInteger bigAmount = new BigInteger(amount);
        return erc20Service.approve(spender, bigAmount)
                .thenApply(receipt -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("transactionHash", receipt.getTransactionHash());
                    response.put("blockNumber", receipt.getBlockNumber().toString());
                    response.put("gasUsed", receipt.getGasUsed().toString());
                    response.put("status", receipt.getStatus());
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Transfer tokens from one address to another (requires prior approval)
     * @param from the address to transfer tokens from
     * @param to the address to transfer tokens to
     * @param amount the amount of tokens to transfer
     * @return the transaction receipt details
     */
    @PostMapping("/transfer-from")
    public CompletableFuture<ResponseEntity<Map<String, String>>> transferFrom(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String amount) {
        BigInteger bigAmount = new BigInteger(amount);
        return erc20Service.transferFrom(from, to, bigAmount)
                .thenApply(receipt -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("transactionHash", receipt.getTransactionHash());
                    response.put("blockNumber", receipt.getBlockNumber().toString());
                    response.put("gasUsed", receipt.getGasUsed().toString());
                    response.put("status", receipt.getStatus());
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Get the amount of tokens that an owner has approved for a spender
     * @param owner the address that owns the tokens
     * @param spender the address that is allowed to spend tokens
     * @return the approved amount
     */
    @GetMapping("/allowance")
    public CompletableFuture<ResponseEntity<Map<String, String>>> allowance(
            @RequestParam String owner,
            @RequestParam String spender) {
        return erc20Service.allowance(owner, spender)
                .thenApply(amount -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("allowance", amount.toString());
                    response.put("owner", owner);
                    response.put("spender", spender);
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Get the total supply of tokens
     * @return the total supply
     */
    @GetMapping("/total-supply")
    public CompletableFuture<ResponseEntity<Map<String, String>>> totalSupply() {
        return erc20Service.totalSupply()
                .thenApply(supply -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("totalSupply", supply.toString());
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Get the token name
     * @return the token name
     */
    @GetMapping("/name")
    public CompletableFuture<ResponseEntity<Map<String, String>>> name() {
        return erc20Service.name()
                .thenApply(tokenName -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("name", tokenName);
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Get the token symbol
     * @return the token symbol
     */
    @GetMapping("/symbol")
    public CompletableFuture<ResponseEntity<Map<String, String>>> symbol() {
        return erc20Service.symbol()
                .thenApply(tokenSymbol -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("symbol", tokenSymbol);
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Get the token decimals
     * @return the token decimals
     */
    @GetMapping("/decimals")
    public CompletableFuture<ResponseEntity<Map<String, String>>> decimals() {
        return erc20Service.decimals()
                .thenApply(tokenDecimals -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("decimals", tokenDecimals.toString());
                    response.put("contractAddress", erc20Service.getContractAddress());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Get the address of the currently loaded contract
     * @return the contract address
     */
    @GetMapping("/address")
    public ResponseEntity<Map<String, String>> getContractAddress() {
        String address = erc20Service.getContractAddress();
        Map<String, String> response = new HashMap<>();
        if (address != null) {
            response.put("contractAddress", address);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "No contract loaded");
            return ResponseEntity.ok(response);
        }
    }
}