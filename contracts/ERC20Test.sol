// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;


contract ERC20Test {
    // 代币核心属性
    string private _name;       
    string private _symbol;     
    uint8 private _decimals;    // 精度（默认18）
    uint256 private _totalSupply;  // 总供应量

    // 余额存储：地址 => 余额
    mapping(address => uint256) private _balances;

    // 授权存储：所有者 => 被授权者 => 授权额度
    mapping(address => mapping(address => uint256)) private _allowances;


    // 事件定义（ERC20 标准要求）
    event Transfer(address indexed from, address indexed to, uint256 value);
    event Approval(address indexed owner, address indexed spender, uint256 value);


    // 构造函数：初始化代币属性
    constructor() {
        _name = "WYZToken";      
        _symbol = "WYZ";         
        _decimals = 18;         // 默认精度18位
        _totalSupply = 0;       // 初始总供应量为0，通过mint发行
    }


    // ==================== ERC20 标准接口实现 ====================

    // 1. 获取代币名称
    function name() public view returns (string memory) {
        return _name;
    }

    // 2. 获取代币符号
    function symbol() public view returns (string memory) {
        return _symbol;
    }

    // 3. 获取精度（默认18）
    function decimals() public view returns (uint8) {
        return _decimals;
    }

    // 4. 获取总供应量
    function totalSupply() public view returns (uint256) {
        return _totalSupply;
    }

    // 5. 查询地址余额
    function balanceOf(address _owner) public view returns (uint256 balance) {
        return _balances[_owner];
    }

    // 6. 转账代币（从调用者地址到目标地址）
    function transfer(address _to, uint256 _value) public returns (bool success) {
        // 检查接收地址不为0
        require(_to != address(0), "ERC20: transfer to the zero address");
        // 检查调用者余额充足
        require(_balances[msg.sender] >= _value, "ERC20: insufficient balance");

        // 扣减发送者余额，增加接收者余额
        _balances[msg.sender] -= _value;
        _balances[_to] += _value;

        // 触发Transfer事件
        emit Transfer(msg.sender, _to, _value);
        return true;
    }

    // 7. 授权转账（从授权地址到目标地址）
    function transferFrom(
        address _from,
        address _to,
        uint256 _value
    ) public returns (bool success) {
        // 检查接收地址不为0
        require(_to != address(0), "ERC20: transfer to the zero address");
        // 检查授权地址余额充足
        require(_balances[_from] >= _value, "ERC20: insufficient balance");
        // 检查授权额度充足
        require(_allowances[_from][msg.sender] >= _value, "ERC20: allowance exceeded");

        // 扣减授权额度
        _allowances[_from][msg.sender] -= _value;
        // 扣减授权地址余额，增加接收者余额
        _balances[_from] -= _value;
        _balances[_to] += _value;

        // 触发Transfer事件
        emit Transfer(_from, _to, _value);
        return true;
    }

    // 8. 授权第三方使用代币
    function approve(address _spender, uint256 _value) public returns (bool success) {
        // 检查被授权地址不为0
        require(_spender != address(0), "ERC20: approve to the zero address");

        // 设置授权额度
        _allowances[msg.sender][_spender] = _value;

        // 触发Approval事件
        emit Approval(msg.sender, _spender, _value);
        return true;
    }

    // 9. 查询授权额度
    function allowance(
        address _owner,
        address _spender
    ) public view returns (uint256 remaining) {
        return _allowances[_owner][_spender];
    }


    // ==================== 扩展功能：mint（发行）和 burn（销毁） ====================

    // 发行代币（增加总供应量并转入目标地址）
    function mint(address _to, uint256 _amount) public {
        require(_to != address(0), "ERC20: mint to the zero address");

        // 增加总供应量和目标地址余额
        _totalSupply += _amount;
        _balances[_to] += _amount;

        // 触发Transfer事件（从0地址转入，代表发行）
        emit Transfer(address(0), _to, _amount);
    }

    // 销毁代币（从调用者地址销毁，减少总供应量）
    function burn(uint256 _amount) public {
        require(_amount > 0, "ERC20: burn amount must be positive");
        require(_balances[msg.sender] >= _amount, "ERC20: burn amount exceeds balance");

        // 减少调用者余额和总供应量
        _balances[msg.sender] -= _amount;
        _totalSupply -= _amount;

        // 触发Transfer事件（转入0地址，代表销毁）
        emit Transfer(msg.sender, address(0), _amount);
    }
}