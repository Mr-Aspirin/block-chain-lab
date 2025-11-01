import { HardhatRuntimeEnvironment } from "hardhat/types";
import { DeployFunction } from "hardhat-deploy/types";
import { Contract } from "ethers";

/**
 * Deploys the ERC20Test contract
 *
 * @param hre HardhatRuntimeEnvironment object.
 */
const deployERC20Test: DeployFunction = async function (hre: HardhatRuntimeEnvironment) {
  /*
    On localhost, the deployer account is the one that comes with Hardhat, which is already funded.

    When deploying to live networks (e.g `yarn deploy --network sepolia`), the deployer account
    should have sufficient balance to pay for the gas fees for contract creation.
  */
  const { deployer } = await hre.getNamedAccounts();
  const { deploy } = hre.deployments;

  // 部署ERC20Test合约（构造函数无参数）
  await deploy("ERC20Test", {
    from: deployer,
    // 合约构造函数无参数，因此args为空数组
    args: [],
    log: true,
    // 本地网络自动挖矿加速部署
    autoMine: true,
  });

  // 获取部署后的合约实例以验证部署结果
  const erc20Test = await hre.ethers.getContract<Contract>("ERC20Test", deployer);
  console.log("✅ ERC20Test deployed at address:", await erc20Test.address);
  // 验证代币基本信息
  console.log("📛 Token name:", await erc20Test.name());
  console.log("🔣 Token symbol:", await erc20Test.symbol());
  console.log("🎯 Decimals:", await erc20Test.decimals());
  console.log("🌐 Initial total supply:", await erc20Test.totalSupply());
};

export default deployERC20Test;

// 部署标签，用于指定部署此合约（例如：yarn deploy --tags ERC20Test）
deployERC20Test.tags = ["ERC20WYZ202330551721"];
