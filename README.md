
# quarrier

建库模块

A.	建库主线程或者实时添加文档的用户线程创建doc对象添加到creator中，creator维护一个线程安全的doc list，当文档数达到阈值时，创建一个createTask并清空列表，添加到调度中心（任务调度中心就是我之前讲的线程池）。

B.	工作线程获取createTask并执行，为这批doc创建倒排索引和正排索引，并创建segment对象持有倒排索引和正排索引。创建完后将segment添加到Merger对象中。

C.	Merger对象接收segment对象，并保存在一个多线程安全队列中，当segment数量达到一个阈值时，创建一个mergeTask并清空列表，然后将mergeTask添加到任务调度中心。

D.	工作线程获取mergeTask，将这批segment合并成一个更大的segment，通过判断设置的合并深度或者segment大小，或者将segment添加到merge对象中，或者将segment添加到writer对象中

E.	Writer对象接受segment对象，也维护一个多线程安全的segment列表，如果segment数量大于阈值，创建writerTask，清空列表，并将writerTask添加到任务调度中心。

F.	工作线程获取writertask，对这一批segment执行写磁盘的操作。


