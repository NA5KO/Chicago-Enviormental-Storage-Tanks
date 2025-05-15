import dash
from dash import dcc, html
import dash_bootstrap_components as dbc
import pandas as pd
import plotly.express as px

# Load datasets
capacity_df = pd.read_csv("data/capacity_merged.csv")
net_df = pd.read_csv("data/net_merged.csv")
pivot_df = pd.read_csv("data/pivot_merged.csv")
products_df = pd.read_csv("data/top_products.csv")
suburb_df = pd.read_csv("data/suburb.csv")
trend_df = pd.read_csv("data/trend.csv")

app = dash.Dash(__name__, external_stylesheets=[dbc.themes.BOOTSTRAP])
app.title = "Chicago Tank Insights"

def capacity_chart():
    fig = px.bar(capacity_df, x="Capacity Range", y="Count", color="Capacity Range", title="Tank Distribution by Capacity")
    return dcc.Graph(figure=fig)

def net_growth_chart():
    fig = px.line(net_df, x="Year", y=["Installations", "Removals", "Net"], title="Installations vs Removals")
    return dcc.Graph(figure=fig)

def tank_type_trend_chart():
    fig = px.area(pivot_df, x="Year", y=["Underground", "Aboveground"], title="Trend of Tank Types Over Time")
    return dcc.Graph(figure=fig)

def top_products_chart():
    fig = px.bar(products_df, x="Count", y="Product", orientation='h', title="Top 10 Products in Storage Tanks")
    return dcc.Graph(figure=fig)

def suburb_distribution_chart():
    fig = px.bar(suburb_df.sort_values("Count", ascending=False), x="Suburb", y="Count", title="Storage Tanks per Suburb")
    return dcc.Graph(figure=fig)

def rolling_trend_chart():
    fig = px.line(trend_df, x="Window", y="5-Year Average", title="5-Year Windowed Installation Trend")
    return dcc.Graph(figure=fig)

# Layout
app.layout = dbc.Container([
    html.H1("Chicago Environmental Tank Dashboard", className="text-center mt-4 mb-4"),
    
    dbc.Tabs([
        dbc.Tab(label="Capacity Distribution", children=[capacity_chart()]),
        dbc.Tab(label="Net Growth", children=[net_growth_chart()]),
        dbc.Tab(label="Tank Type Trends", children=[tank_type_trend_chart()]),
        dbc.Tab(label="Top Products", children=[top_products_chart()]),
        dbc.Tab(label="Suburb Distribution", children=[suburb_distribution_chart()]),
        dbc.Tab(label="5-Year Rolling Trend", children=[rolling_trend_chart()]),
    ])
], fluid=True)
