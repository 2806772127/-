一、环境配置
1.下载并安装7z软件, 将7z.exe所在目录加入到PATH路径
2.下载并安装maven软件， 将maven bin目录（保护mvn命令）加入到PATH路径
3. 经过以上两个个配置，确保以下命令能够在cmd中使用：
	7z
	mvn
	
二、打包
1.运行build_jar.bat会在target目录生成cfc-goldencudgel-web-0.0.1-SNAPSHOT.jar(jar包里的BOOT-INF/lib目录会被删除)。

三、部署
1.将cfc-goldencudgel-web-0.0.1-SNAPSHOT.jar上传至服务器部署路径下(例如：/app/apuser/docker/fubon/jgb)
2.将BOOT-INF/lib的jar包全部上传至服务的部署路径下的BOOT-INF/lib(例如：/app/apuser/docker/fubon/jgb/BOOT-INF/lib)。
之后，如果项目中lib有新增的jar，只需将新增的jar包上传至上传至服务的部署路径下的BOOT-INF/lib(例如：/app/apuser/docker/fubon/jgb/BOOT-INF/lib)即可。
3.启动脚本(start.sh)第二行中追加以下内容
echo "-----jar add lib---------"
jar -uf0 cfc-goldencudgel-web-0.0.1-SNAPSHOT.jar BOOT-INF/lib
4.运行启动脚本即可(例如：./start.sh jgb 10080:10080)
