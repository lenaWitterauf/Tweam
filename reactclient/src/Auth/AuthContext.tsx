import * as React from 'react';

const { Consumer: AuthConsumer, Provider: AuthProvider } = React.createContext({ loggedIn: false });

export { AuthConsumer, AuthProvider };