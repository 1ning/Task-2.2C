from turtle import color, title
import chart_studio.plotly as py
import plotly.graph_objects as go
import numpy as np
import sqlite3 as sql
import pandas as pd
import sqlite3 as sql
from plotly.offline import download_plotlyjs,init_notebook_mode
import cufflinks as cf
import plotly.express as px
import chart_studio.plotly as py 
import plotly.graph_objs as go
init_notebook_mode(connected=True)
cf.go_offline()
conn=sql.connect('H:/database/database2')
df=pd.read_sql_query("""SELECT id, date,temperature FROM tem """, conn)

Celsius = go.Bar(                                                                                                                                                             
  x = df.date,
  y = df.temperature,
  name="Celsius"
  )
Fahrenheit = go.Bar(                                                                                                                                                             
  x = df.date,
  y = df.temperature*1.8 + 32,
  name="Fahrenheit"
  )
data = [Celsius,Fahrenheit]

layout = go.Layout(barmode ='stack',title="Temperature Schedule")
figure=go.Figure(data=data,layout=layout)
figure.update_layout(
    xaxis=dict(
       title="time"
    ),
    yaxis=dict(
       title="Temperature"
    ),)
py.plot(figure, filename = 'tem', auto_open=True)