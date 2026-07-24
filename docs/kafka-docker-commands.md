Below is a complete set of commands using the **Apache Kafka Docker image** in **KRaft mode** (ZooKeeper-free), with a **Docker volume** for persistent data. This is suitable for local development and training.

---

# 1. Pull the Kafka Image

```bash
docker pull apache/kafka:latest
```

Check the image:

```bash
docker images
```

---

# 2. Create a Docker Volume

```bash
docker volume create kafka-data
```

Verify:

```bash
docker volume ls
```

---

# 3. Run Kafka Container

```bash
docker run -d --name kafka -p 9092:9092  -v kafka-data:/var/lib/kafka/data
  apache/kafka:latest
```

Verify container:

```bash
docker ps
```

View logs:

```bash
docker logs -f kafka
```

---

# 4. Enter the Kafka Container

```bash
docker exec -it kafka bash
```

Most Kafka utilities are available under:

```bash
/opt/kafka/bin
```

For convenience:

```bash
cd /opt/kafka/bin
```

---

# 5. List Existing Topics

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --list
```

---

# 6. Create a Topic

Create a topic named **orders** with one partition.

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic orders --partitions 1 --replication-factor 1
```

Expected output:

```
Created topic orders.
```

---

# 7. Describe a Topic

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic orders
```

Example:

```
Topic: orders
PartitionCount:1
ReplicationFactor:1
```

---

# 8. Produce (Send) Messages

```bash
./kafka-console-producer.sh --bootstrap-server localhost:9092 --topic orders
```

Type messages:

```
Order-1001
Order-1002
Order-1003
```

Exit:

```
Ctrl+C
```

---

# 9. Consume Messages

Read from the beginning:

```bash
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic orders --from-beginning
```

Exit:

```
Ctrl+C
```

---

# 10. Create Multiple Partitions

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic payments --partitions 3 --replication-factor 1
```

Describe:

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic payments
```

---

# 11. Increase Partitions

Suppose the topic currently has 3 partitions.

Increase to 6:

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --alter --topic payments --partitions 6
```

Verify:

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic payments
```

> **Note:** Kafka allows increasing the number of partitions but does **not** support decreasing them.

---

# 12. Delete a Topic

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic payments
```

List topics:

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 --list
```

---

# 13. Produce Messages with Keys

```bash
./kafka-console-producer.sh \
--bootstrap-server localhost:9092 \
--topic orders \
--property "parse.key=true" \
--property "key.separator=:"
```

Example:

```
1001:Book
1002:Laptop
1003:Phone
```

---

# 14. Consume Showing Keys

```bash
./kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic orders \
--from-beginning \
--property print.key=true
```

Output:

```
1001    Book
1002    Laptop
1003    Phone
```

---

# 15. List Consumer Groups

```bash
./kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--list
```

---

# 16. Describe a Consumer Group

```bash
./kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--describe \
--group console-consumer
```

---

# 17. Delete Consumer Group Offsets

```bash
./kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--delete-offsets \
--group my-group \
--topic orders
```

---

# 18. Delete the Kafka Container

```bash
docker stop kafka
```

```bash
docker rm kafka
```

The volume remains intact.

---

# 19. Remove the Persistent Volume

```bash
docker volume rm kafka-data
```

---

# 20. Restart Kafka

```bash
docker start kafka
```

---

# 21. Useful Docker Commands

Container status:

```bash
docker ps
```

All containers:

```bash
docker ps -a
```

Logs:

```bash
docker logs kafka
```

Interactive shell:

```bash
docker exec -it kafka bash
```

---

# Common Kafka Topic Commands

| Operation               | Command                                  |
| ----------------------- | ---------------------------------------- |
| List topics             | `kafka-topics.sh --list`                 |
| Create topic            | `kafka-topics.sh --create`               |
| Describe topic          | `kafka-topics.sh --describe`             |
| Delete topic            | `kafka-topics.sh --delete`               |
| Increase partitions     | `kafka-topics.sh --alter --partitions N` |
| Produce messages        | `kafka-console-producer.sh`              |
| Consume messages        | `kafka-console-consumer.sh`              |
| List consumer groups    | `kafka-consumer-groups.sh --list`        |
| Describe consumer group | `kafka-consumer-groups.sh --describe`    |

### Notes

* The `apache/kafka:latest` image runs Kafka in **KRaft mode** by default, so no ZooKeeper container is required.
* `--replication-factor 1` is appropriate for a single-node Kafka instance. In a production cluster, use a replication factor of **3** or higher.
* The named Docker volume (`kafka-data`) preserves topics and messages across container restarts and recreations as long as the volume is not removed.
