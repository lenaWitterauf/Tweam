import {Button, Typography} from '@material-ui/core';
import * as React from 'react';
import {authService} from '../Auth/AuthService';
import {CenterCard} from '../CenterCard';
import {VerticalSpacer} from '../Spacer';
import {RouterProps} from "react-router";
import {networkService} from "../Network/NetworkService";

export function Management(props: RouterProps): React.ReactElement {
    const handleSubmit = () => {
        networkService.updateMatches().then(done => {
            if (done) {
                props.history.push(`/teams`);
            }
        })

    };

    return (
        <CenterCard>
            <VerticalSpacer height={20}/>
            <Typography variant="h4" component="h3" style={{textAlign: 'center'}}>Recalculate Matchings</Typography>
            <VerticalSpacer height={16}/>
            <Button variant="contained" color="primary" onClick={handleSubmit}
                    disabled={authService.loggedIn}>Match!</Button>
            <VerticalSpacer height={20}/>
        </CenterCard>
    );
}