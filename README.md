# Chicago Environmental Storage Tanks Analysis

A comprehensive big data analytics project analyzing environmental storage tank data from Chicago. This project demonstrates the complete big data ecosystem workflow from data ingestion to visualization, utilizing Hadoop MapReduce, Apache Spark, HBase, and interactive dashboards.

## 📊 Project Overview

This project analyzes Chicago's environmental storage tank dataset, tracking installations, removals, tank types, capacities, products stored, and geographical distribution across the city. The analysis provides insights into:

- Historical trends of tank installations and removals
- Tank capacity distributions and product types
- Geographic distribution across Chicago suburbs
- Environmental compliance and safety patterns
- Long-term infrastructure trends

## 🛠️ Technology Stack

### Big Data Processing
- **Apache Hadoop** (v3.3.6) - Distributed storage and MapReduce processing
- **Apache Spark** (v3.4.1) - Fast in-memory analytics and data processing
- **Apache HBase** (v2.5.8) - NoSQL distributed database for tank data storage

### Programming Languages
- **Java 8** - Primary language for MapReduce and Spark jobs
- **Python 3** - Data visualization and dashboard development

### Data Visualization
- **Plotly** - Interactive charts and graphs
- **Dash** - Web-based analytics dashboard
- **Dash Bootstrap Components** - UI framework
- **ydata-profiling** - Automated data profiling reports

### Build & Dependency Management
- **Apache Maven** - Project build and dependency management
- **Maven Shade Plugin** - Creating uber-JARs for cluster deployment

### Data Format
- **CSV** - Input data format
- **HDFS** - Distributed file system storage

## 📁 Project Structure

The project is organized across multiple branches, each representing a different phase of the big data pipeline:

### Branch: `master` - Hadoop MapReduce Jobs
**Purpose**: Initial data processing using Hadoop MapReduce framework

**Key Components**:
- `TanksByProduct.java` - Counts storage tanks by product type
- `TankCapacityAnalysis.java` - Analyzes tank capacity distributions
- `InstallationsPerYear.java` - Tracks installation trends over time
- `RemovalsPerYear.java` - Tracks removal trends over time
- `TankTypeDistribution.java` - Analyzes distribution of tank types (underground vs. aboveground)
- `InstallationsPerSuburb.java` - Geographic distribution analysis

**Achievements**:
- ✅ Implemented 6+ MapReduce jobs for comprehensive data analysis
- ✅ Processed CSV data with proper field parsing
- ✅ Generated aggregated outputs for downstream analysis

### Branch: `Hbase` - NoSQL Data Storage
**Purpose**: Load structured tank data into HBase for fast random access

**Key Components**:
- `HelloHBase.java` - Data ingestion pipeline from CSV to HBase

**Data Schema**:
- **Row Key**: `facilityId#tankId` (composite key)
- **Column Families**:
  - `info` - Tank metadata (owner, type, material, construction)
  - `location` - Geographic data (address, suburb)
  - `status` - Temporal data (installation date, removal date)
  - `product` - Product details (tank product, capacity)

**Achievements**:
- ✅ Designed efficient HBase schema with logical column families
- ✅ Implemented CSV-to-HBase ETL pipeline
- ✅ Configured ZooKeeper integration for distributed coordination

### Branch: `Spark` - Advanced Analytics
**Purpose**: High-performance data analytics using Apache Spark

**Key Spark Jobs**:
1. **NetGrowthJob.java** - Calculates net growth (installations - removals) by year
2. **TopProductsJob.java** - Identifies most common products stored in tanks
3. **TrendJob.java** - Computes 5-year rolling average of installations
4. **CapacityBucketsJob.java** - Categorizes tanks by capacity ranges
5. **Additional analyses** - Building on MapReduce jobs with Spark optimization

**Technical Highlights**:
- Spark SQL for declarative data transformations
- DataFrame API for efficient processing
- Date parsing and temporal analysis
- Aggregation and windowing functions

**Achievements**:
- ✅ Migrated MapReduce jobs to Spark for 10-100x performance improvement
- ✅ Implemented advanced analytics (rolling averages, bucketing)
- ✅ Generated cleaned datasets for visualization

### Branch: `spark-output` - Processed Data Outputs
**Purpose**: Stores merged and cleaned outputs from Spark jobs

**Output Files**:
- `net_merged.csv` - Year-by-year installations, removals, and net growth
- `top_products.csv` - Top products by tank count
- `capacity_merged.csv` - Tank distribution by capacity buckets
- `pivot_merged.csv` - Tank type trends (underground vs. aboveground) over time
- `trend.csv` - 5-year moving average of installations
- `suburb.csv` - Geographic distribution across suburbs

**Achievements**:
- ✅ Consolidated 100+ Spark output partitions into single CSV files
- ✅ Clean, ready-to-visualize datasets
- ✅ Standardized schema across all outputs

### Branch: `viz` - Interactive Dashboard
**Purpose**: Web-based interactive dashboard for data exploration

**Dashboard Features**:
1. **Capacity Distribution** - Bar chart showing tank counts by capacity range
2. **Net Growth Analysis** - Line chart comparing installations vs. removals over time
3. **Tank Type Trends** - Area chart showing evolution of underground vs. aboveground tanks
4. **Top Products** - Horizontal bar chart of most common stored products
5. **Suburb Distribution** - Geographic analysis of tank distribution
6. **5-Year Rolling Trend** - Smoothed installation trend analysis
7. **Automated Data Profiling** - ydata-profiling reports for each dataset

**Technical Stack**:
- Dash framework with Bootstrap styling
- Plotly for interactive visualizations
- Tabbed interface for organized navigation
- Embedded profiling reports for data quality insights

**Achievements**:
- ✅ Built fully interactive web dashboard
- ✅ Integrated 6 different visualization types
- ✅ Automated data profiling for quality assurance
- ✅ Responsive design with Bootstrap components

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven 3.6+
- Apache Hadoop 3.3.6 (for MapReduce jobs)
- Apache Spark 3.4.1 (for Spark jobs)
- Apache HBase 2.5.8 (for database operations)
- Python 3.8+ (for visualization dashboard)

### Building the Project

#### MapReduce Jobs (master branch)
```bash
git checkout master
mvn clean package
# Run example job
hadoop jar target/chicago_env-1.0-SNAPSHOT.jar bigdataproject.jobs.TanksByProduct input/file.csv output/products
```

#### Spark Jobs (Spark branch)
```bash
git checkout Spark
mvn clean package
# Run example Spark job
spark-submit --class bigdataproject.jobs.NetGrowthJob target/chicago_env-1.0-SNAPSHOT.jar
```

#### HBase Data Loading (Hbase branch)
```bash
git checkout Hbase
# Ensure HBase and ZooKeeper are running
java -cp target/chicago_env-1.0-SNAPSHOT.jar tn.insat.tp4.HelloHBase
```

#### Visualization Dashboard (viz branch)
```bash
git checkout viz
pip install -r requirements.txt
python app.py
# Access dashboard at http://localhost:8050
```

## 📈 Key Insights from Analysis

Based on the processed data outputs:

1. **Product Distribution**:
   - Gasoline: 5,546 tanks (largest category)
   - Heating Oil: 3,678 tanks
   - Diesel: 2,182 tanks
   - Fuel Oil: 860 tanks

2. **Historical Trends**:
   - Data spans from 1901 to present
   - Significant installation activity in mid-20th century
   - Recent trends show declining installations

3. **Tank Types**:
   - Predominance of underground storage tanks
   - Shift in tank technology over decades
   - Safety and environmental compliance evolution

## 🔄 Data Pipeline Workflow

```
Raw CSV Data
    ↓
[Hadoop MapReduce] → Initial aggregations and filtering
    ↓
[Apache HBase] → Structured NoSQL storage with fast access
    ↓
[Apache Spark] → Advanced analytics and transformations
    ↓
[Processed CSVs] → Clean, merged datasets
    ↓
[Dash Dashboard] → Interactive visualizations and insights
```

## 📊 Dataset Information

**Source**: Chicago Environmental Storage Tanks dataset

**Fields**:
- Address information (street, number, direction)
- Tank specifications (type, material, construction)
- Product and capacity details
- Temporal data (installation, removal, last used dates)
- Geographic coordinates (latitude, longitude)
- Facility and owner information
- Regulatory and compliance data

## 🤝 Contributing

This project demonstrates a complete big data analytics workflow. Contributions are welcome for:
- Additional analytics jobs
- Enhanced visualizations
- Performance optimizations
- Documentation improvements

## 📝 License

This project is for educational and analytical purposes, demonstrating big data technologies and workflows.

## 🎯 Learning Outcomes

This project demonstrates proficiency in:
- ✅ Distributed computing with Hadoop MapReduce
- ✅ Real-time analytics with Apache Spark
- ✅ NoSQL database design with HBase
- ✅ ETL pipeline development
- ✅ Data visualization and dashboard creation
- ✅ Maven project management
- ✅ Big data ecosystem integration
- ✅ Java and Python development for data engineering

---

**Project Status**: Complete and functional across all branches

**Last Updated**: February 2026