import dash
from dash import dcc, html
import dash_bootstrap_components as dbc
import pandas as pd
import plotly.express as px
from ydata_profiling import ProfileReport
import tempfile
import os

# Load datasets
capacity_df = pd.read_csv("data/capacity_merged.csv")
net_df = pd.read_csv("data/net_merged.csv")
pivot_df = pd.read_csv("data/pivot_merged.csv")
products_df = pd.read_csv("data/top_products.csv")
suburb_df = pd.read_csv("data/suburb.csv", sep="\t", header=None, names=["Suburb", "Count"])
trend_df = pd.read_csv("data/trend.csv")

app = dash.Dash(__name__, external_stylesheets=[dbc.themes.BOOTSTRAP])
app.title = "Chicago Tank Insights"

def capacity_chart():
    fig = px.bar(capacity_df, x="capBucket", y="count", color="capBucket", title="Tank Distribution by Capacity")
    return dcc.Graph(figure=fig)

def net_growth_chart():
    fig = px.line(net_df, x="year", y=["installs", "removals", "net"], title="Installations vs Removals")
    return dcc.Graph(figure=fig)

def tank_type_trend_chart():
    fig = px.area(
        pivot_df,
        x="year",
        y=["ABOVEGROUND STORAGE TANK", "UNDERGROUND STORAGE TANK"],
        title="Trend of Tank Types Over Time"
    )
    return dcc.Graph(figure=fig)

def top_products_chart():
    fig = px.bar(products_df, x="count", y="product", orientation='h', title="Top 10 Products in Storage Tanks")
    return dcc.Graph(figure=fig)

def suburb_distribution_chart():
    fig = px.bar(suburb_df.sort_values("Count", ascending=False), x="Suburb", y="Count", title="Storage Tanks per Suburb")
    return dcc.Graph(figure=fig)

def rolling_trend_chart():
    fig = px.line(trend_df, x="year", y="mov_avg", title="5-Year Windowed Installation Trend")
    return dcc.Graph(figure=fig)

def profiling_report_component(df, title):
    profile = ProfileReport(
        df,
        title=title,
        explorative=True
    )
    with tempfile.NamedTemporaryFile(delete=False, suffix=".html") as tmpfile:
        profile.to_file(tmpfile.name)
        tmpfile.seek(0)
        html_content = tmpfile.read().decode("utf-8")
    os.unlink(tmpfile.name)
    return html.Div([
        html.H3(title),
        html.Iframe(srcDoc=html_content, style={"width": "100%", "height": "1000px", "border": "none"})
    ])

# Layout
app.layout = dbc.Container([
    html.H1("Chicago Environmental Tank Dashboard", className="text-center mt-4 mb-4"),
    dbc.Tabs([
        dbc.Tab(label="Capacity Distribution", children=[
            dbc.Tabs([
                dbc.Tab(label="Visualization", children=[capacity_chart()]),
                dbc.Tab(label="Data Report", children=[profiling_report_component(capacity_df, "Capacity Data Profiling Report")]),
            ])
        ]),
        dbc.Tab(label="Net Growth", children=[
            dbc.Tabs([
                dbc.Tab(label="Visualization", children=[net_growth_chart()]),
                dbc.Tab(label="Data Report", children=[profiling_report_component(net_df, "Net Growth Data Profiling Report")]),
            ])
        ]),
        dbc.Tab(label="Tank Type Trends", children=[
            dbc.Tabs([
                dbc.Tab(label="Visualization", children=[tank_type_trend_chart()]),
                dbc.Tab(label="Data Report", children=[profiling_report_component(pivot_df, "Tank Type Trends Data Profiling Report")]),
            ])
        ]),
        dbc.Tab(label="Top Products", children=[
            dbc.Tabs([
                dbc.Tab(label="Visualization", children=[top_products_chart()]),
                dbc.Tab(label="Data Report", children=[profiling_report_component(products_df, "Top Products Data Profiling Report")]),
            ])
        ]),
        dbc.Tab(label="Suburb Distribution", children=[
            dbc.Tabs([
                dbc.Tab(label="Visualization", children=[suburb_distribution_chart()]),
                dbc.Tab(label="Data Report", children=[profiling_report_component(suburb_df, "Suburb Distribution Data Profiling Report")]),
            ])
        ]),
        dbc.Tab(label="5-Year Rolling Trend", children=[
            dbc.Tabs([
                dbc.Tab(label="Visualization", children=[rolling_trend_chart()]),
                dbc.Tab(label="Data Report", children=[profiling_report_component(trend_df, "5-Year Rolling Trend Data Profiling Report")]),
            ])
        ]),
    ])
], fluid=True)

if __name__ == "__main__":
    app.run(debug=True)
