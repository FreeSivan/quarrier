
# quarrier 采石匠

这是一个尝试工程，对二进制文件建索引

整个系统以一个线程池为调度中心，将建子索引， merge子索引， 加载， 检索，等操作统统作为task添加到调度中心。

可以支持多线程建库，多线程merge， 多线程加载， 多线程查询等

当前完成：build部分

难点很多，等待后续思考
