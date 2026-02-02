import { Card, CardContent, Grid, Typography } from '@mui/material';
import React, {useEffect, useState } from 'react'
import { useNavigate } from 'react-router';
// import { useNavigate } from 'react-router-dom';
import { getActivities } from '../services/api';

const ActivityList = () => {

const [activities, setActivities] = useState([]);
const navigate = useNavigate();

  const fetchActivities = async () => {
    try {
      const response = await getActivities();
      setActivities(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  return (
    <Grid container spacing={2}> 
    {activities.map((activity) => (
     // <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }} >
     <Grid item xs={12} key={activity.id}>  
     <Card sx={{cursor: 'pointer'}}
           //</Grid> onClick= {() => navigate(`/activities/${activity.id}`)}>
           onClick={() => {
            console.log("Navigating with activityId:", activity.id);
            navigate(`/activities/${activity.id}`);
          }}
        >
           <CardContent>
            <Typography variant="h6">{activity.type}</Typography>
            <Typography >Duration: {activity.duration} minutes</Typography>
            <Typography >Calories Burned: {activity.caloriesBurned}</Typography>
          </CardContent>
        </Card>
      </Grid>
    ))}
    </Grid>
  )
}

export default ActivityList
